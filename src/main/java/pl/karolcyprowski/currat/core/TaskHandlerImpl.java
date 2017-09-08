package pl.karolcyprowski.currat.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.karolcyprowski.currat.entities.ExchangeRate;
import pl.karolcyprowski.currat.monitor.NBPMonitor;
import pl.karolcyprowski.currat.nbp.NBPData;
import pl.karolcyprowski.currat.service.DBService;
import pl.karolcyprowski.currat.service.NBPService;
import pl.karolcyprowski.currat.spark.SparkTool;
import pl.karolcyprowski.currat.spark.SparkToolImpl;

public class TaskHandlerImpl implements TaskHandler {
	
	static Logger logger = Logger.getLogger(TaskHandlerImpl.class);
	
	private static final int DAYS_TO_PREDICT = 10;
	
	@Autowired
	private DBService dbService;
	
	@Autowired
	private NBPService nbpService;
	
	@Autowired
	private DataProcessor dataProcessor;
	
	@Autowired
	private SparkTool sparkTool;
	
	@Value("${pl.karolcyprowski.initial_time_period}")
	private int initialTimePeriod;
	
	private ExchangeRateSeriesModel initialExchangeRateSeriesModel;
	
	private Map<String, List<Double>> forecasts;
	
	public TaskHandlerImpl() {
		
	}

	public DBService getDBService() {
		return dbService;
	}

	public NBPService getNBPService() {
		return nbpService;
	}
	
	public void manageCoreTasks() {
		performInitOperation();
	}
	
	private void performInitOperation () {
		boolean hasCurrentData = dbService.hasCurrentData();
		if(!hasCurrentData) {
			loadDataFromWeb();
		}
		if(initialExchangeRateSeriesModel == null) {
			initialExchangeRateSeriesModel = generateInitialExchangeRateSeriesModel();
		}
	}
	
	public void loadRecentDataFromWeb() {
		List<NBPData> dataFromNBP = nbpService.getRecentDataFromNBP();
		insertDataFromWeb(dataFromNBP);
	}
	
	public void loadDataFromWeb() {
		List<NBPData> dataFromNBP = nbpService.getAllDataFromNBP();
		insertDataFromWeb(dataFromNBP);
	}
	
	public void insertDataFromWeb(List<NBPData> dataFromWeb) {
		List<ExchangeRate> exchangeRates = dataProcessor.parseNBPDataList(dataFromWeb);
		logger.info("Rows to insert: " + exchangeRates.size());
		dbService.insertData(exchangeRates);
	}
	
	public ExchangeRateSeriesModel getExchangeRateSeriesModelForTimePeriod(Date startDate, Date endDate) {
		List<ExchangeRate> exchangeRates = dbService.getExchangeRates(startDate, endDate, null);
		if (forecasts == null) {
			initForecasts();
		}
//		model.setForecasts(forecasts);
		ExchangeRateSeriesModel model = new ExchangeRateSeriesModelImpl(startDate, endDate, exchangeRates, forecasts);
		
		return model;
	}
	
	public ExchangeRateSeriesModel getInitialExchangeRateSeriesModel() {
		if(initialExchangeRateSeriesModel == null) {
			initialExchangeRateSeriesModel = generateInitialExchangeRateSeriesModel();
		}
		return initialExchangeRateSeriesModel;
	}
	
	private ExchangeRateSeriesModel generateInitialExchangeRateSeriesModel() {
		Calendar calendar = Calendar.getInstance();
		Date endDate = calendar.getTime();
		calendar.add(Calendar.DATE, initialTimePeriod * (-1));
		Date startDate = calendar.getTime();
		return getExchangeRateSeriesModelForTimePeriod(startDate, endDate);
	}
	
	public List<ExchangeRate> getRatesForCurrencyCode (String code) {
		return dbService.getExchangeRates(code);
	}
	
	public void initForecasts() {
		Map<String, List<Double>> newForecasts = new HashMap<String, List<Double>>();
		String[] codes = {"USD", "EUR", "CHF" 
//				,"GBP", "RUB", "UAH"
				};
		if(sparkTool == null) {
			sparkTool = new SparkToolImpl();
		}
		for(int i = 0; i < codes.length; i++) {
			String code = codes[i];
			List<ExchangeRate> exchangeRates = dbService.getExchangeRates(code);
			List<Double> ratesDoubleList = new ArrayList<Double>();
			Collections.sort(exchangeRates);
			exchangeRates.stream().forEach(e -> ratesDoubleList.add(e.getRate()));
			List<Double> forecast = predictRateSeries(ratesDoubleList);
			newForecasts.put(code, forecast);
		}
		forecasts = newForecasts;
	}
	
	private List<Double> predictRateSeries(List<Double> source) {
		List<Double> predictedValues = sparkTool.getForecastList(DAYS_TO_PREDICT, source);
		logger.info(predictedValues);
		return predictedValues;
	}
}
