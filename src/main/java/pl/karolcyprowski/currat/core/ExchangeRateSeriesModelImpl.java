package pl.karolcyprowski.currat.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import pl.karolcyprowski.currat.entities.ExchangeRate;
import pl.karolcyprowski.currat.spark.SparkTool;
import pl.karolcyprowski.currat.spark.SparkToolImpl;

public class ExchangeRateSeriesModelImpl implements ExchangeRateSeriesModel {
	
	static Logger logger = Logger.getLogger(ExchangeRateSeriesModelImpl.class);
	
	@Autowired
	private SparkTool sparkTool;

	private Date startDate;
	
	private Date endDate;
	
	private Map<String, List<Double>> exchangeRateSeriesMap;
	
	private Map<String, List<Double>> forecasts;
	
	private List<String> dataLabels;
	
	public static final String USD_CODE = "USD";
	
	public static final int DAYS_TO_PREDICT = 10;
	
	public ExchangeRateSeriesModelImpl(Date startDate, Date endDate, List<ExchangeRate> exchangeRateList, Map<String, List<Double>> forecasts) {
		this.forecasts = forecasts;
		this.startDate = startDate;
		this.endDate = endDate;
		initExchangeRateSeriesModel(exchangeRateList);
	}
	
	private void initExchangeRateSeriesModel(List<ExchangeRate> exchangeRateList) {
		Map<String, List<Double>> newMap = new HashMap<String, List<Double>>();
		List<String> newDateLabels = new ArrayList<String>();
		
		Collections.sort(exchangeRateList);
		List<String> currencyCodes = exchangeRateList.stream()
				.map(ExchangeRate::getCurrencyCode)
				.collect(Collectors.toList());
		List<Date> dates = exchangeRateList.stream()
				.map(ExchangeRate::getDate)
				.collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
		Collections.sort(dates);
		Map<String, List<ExchangeRate>> exchangeRatesByCode = exchangeRateList.stream()
				.collect(
						Collectors.groupingBy(
								ExchangeRate::getCurrencyCode)
						);
		int templateSize = exchangeRatesByCode.get(ExchangeRateSeriesModelImpl.USD_CODE).size();
		
		for (Map.Entry<String, List<ExchangeRate>> entry : exchangeRatesByCode.entrySet()) {
//			List<ExchangeRate> singleListForCode = entry.getValue();
//			Collections.sort(singleListForCode);
			String code = entry.getKey();
//			if (singleListForCode.size() < templateSize) {
//				singleListForCode = getEnhancedWithZeros(singleListForCode, dates);
//			}
//			List<Double> exchangeRateSeries = new ArrayList<Double>();
//			for (int i = 0; i < singleListForCode.size(); i++) {
//				ExchangeRate exchangeRate = singleListForCode.get(i);		
//				exchangeRateSeries.add(exchangeRate.getRate());
//			}
			List<Double> exchangeRateSeries = convertExchangeRatesToDouble(entry.getValue(), code, templateSize, dates);
			if(exchangeRateSeries.get(exchangeRateSeries.size() - 1) > 0 && eligibleForPrediction(code)) {
				exchangeRateSeries = predictRateSeries(exchangeRateSeries, code);
			}
			newMap.put(code, exchangeRateSeries);
		}
		
		for (int i = 0; i < dates.size(); i++) {
			String dateAsString = dates.get(i).toString();
			newDateLabels.add(dateAsString);
		}
		for (int i = 0; i < DAYS_TO_PREDICT; i++) {
			newDateLabels.add("+" + (i+1));
		}
		
		this.exchangeRateSeriesMap = newMap;
		this.dataLabels = newDateLabels;
	}
	
	private List<Double> convertExchangeRatesToDouble(List<ExchangeRate> singleListForCode, String code, int templateSize, List<Date> dates) {
		Collections.sort(singleListForCode);
		if (singleListForCode.size() < templateSize) {
			singleListForCode = getEnhancedWithZeros(singleListForCode, dates);
		}
		List<Double> exchangeRateSeries = new ArrayList<Double>();
		for (int i = 0; i < singleListForCode.size(); i++) {
			ExchangeRate exchangeRate = singleListForCode.get(i);		
			exchangeRateSeries.add(exchangeRate.getRate());
		}
		return exchangeRateSeries;
	}
	
	private boolean eligibleForPrediction(String code) {
		if (code.equals("USD") || code.equals("CHF") || code.equals("EUR") || code.equals("GBP") || code.equals("UAH") || code.equals("RUB")) {
			return true;
		}
		return false;
	}
	
	private List<Double> predictRateSeries(List<Double> source, String code) {
		List<Double> forecastsForCode = forecasts.get(code);
		if (forecastsForCode != null) {
			forecastsForCode.stream().forEach(e -> source.add(e));
		}
		return source;
	}
	
	private List<Double> predictRateSeries(List<Double> source) {
		
		if (sparkTool == null) {
			this.sparkTool = new SparkToolImpl();
		}
		List<Double> predictedValues = sparkTool.getForecastList(DAYS_TO_PREDICT, source);
		for (int i = predictedValues.size() - (DAYS_TO_PREDICT + 1); i < predictedValues.size(); i++) {
			source.add(predictedValues.get(i));
		}
		return source;
	}
	
	public List<ExchangeRate> getEnhancedWithZeros(List<ExchangeRate> list, List<Date> dates) {
		Date start = dates.get(0);
		Date end = dates.get(dates.size() - 1);
		String code = list.get(0).getCurrencyCode();
		List<ExchangeRate> newList = new ArrayList<ExchangeRate>();
		int listIndex = 0;
		for (int iDate = 0; iDate < dates.size(); iDate++) {
			Date date = dates.get(iDate);
			if(listIndex < list.size()) {
				ExchangeRate rate = list.get(listIndex);
				if (rate != null) {
					if (date.toString().equals(rate.getDate().toString())) {
						newList.add(rate);
						listIndex++;
					} else {
						newList.add(new ExchangeRate(code, date, 0));
					}
				} else {
					newList.add(new ExchangeRate(code, date, 0));
				}
			} else {
				newList.add(new ExchangeRate(code, date, 0));
			}
		}
		return newList;
	}
	
	@Override
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public Date getEndDate() {
		return endDate;
	}

	@Override
	public Map<String, List<Double>> getExchangeRateSeriesMap() {
		return exchangeRateSeriesMap;
	}
	
	public List<String> getCurrencyCodes() {
		List<String> currencyCodes = new ArrayList<String>();
		exchangeRateSeriesMap.entrySet().forEach(e -> currencyCodes.add(e.getKey()));
		return currencyCodes;
	}
	
	public Map<String, String> getJsonDataPointsMap() {
		Map<String, String> jsonDataPointsMap = new HashMap<String, String>();
		Gson gsonObj = new Gson();
		exchangeRateSeriesMap.entrySet().forEach(
				e -> jsonDataPointsMap.put(e.getKey(), getJsonDataPoints(e.getValue(), gsonObj))
		);
		return jsonDataPointsMap;
	}
	
	private String getJsonDataPoints(List<Double> series, Gson gsonObj) {
		String dataPoints = gsonObj.toJson(series);
		return dataPoints;
	}
	
	public List<String> getDataLabels() {
		return dataLabels;
	}
	
	public void setForecasts(Map<String, List<Double>> forecasts) {
		this.forecasts = forecasts;
	}

}
