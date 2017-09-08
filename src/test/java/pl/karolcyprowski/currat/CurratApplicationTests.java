package pl.karolcyprowski.currat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cloudera.sparkts.models.ARIMA;
import com.cloudera.sparkts.models.ARIMAModel;
import com.google.gson.JsonElement;

import pl.karolcyprowski.currat.core.ExchangeRateSeries;
import pl.karolcyprowski.currat.core.ExchangeRateSeriesModel;
import pl.karolcyprowski.currat.core.TaskHandler;
import pl.karolcyprowski.currat.entities.ExchangeRate;
import pl.karolcyprowski.currat.nbp.NBPData;
import pl.karolcyprowski.currat.nbp.NBPDataImpl;
import pl.karolcyprowski.currat.nbp.NBPRequestBuilder;
import pl.karolcyprowski.currat.nbp.NBPRestConsummerImpl;
import pl.karolcyprowski.currat.service.DBService;
import pl.karolcyprowski.currat.service.NBPService;
import pl.karolcyprowski.currat.service.QueryParameterProcessor;
import pl.karolcyprowski.currat.spark.SparkTool;
import pl.karolcyprowski.currat.spark.SparkToolImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurratApplicationTests {
	
	static Logger logger = Logger.getLogger(CurratApplicationTests.class);
	
	@Autowired
	private NBPRestConsummerImpl nbpRestConsummer;
	
	@Autowired
	private NBPRequestBuilder nbpRequestBuilder;
	
	@Autowired
	private QueryParameterProcessor queryParameterProcessor;
	
	@Autowired
	private TaskHandler taskHandler;
	
	@Autowired
	private DBService dbService;
	
	@Autowired
	private NBPService nbpService;
	
	private List<NBPData> testData;
	

	@Test
	public void contextLoads() {
		taskHandler.manageCoreTasks();
	}
	
	@Test
	public void NBPRestConsummerImplGenerateRequestImpl() {
		String requestFor20170821 = "http://api.nbp.pl/api/exchangerates/tables/A/2017-08-01/2017-08-21/?format=json";
		Calendar calendar = getCalendarForDate(1, Calendar.AUGUST, 2017);
		String request = nbpRequestBuilder.buildPeriodRequestUrl(calendar, 20);
		System.out.println(request);
		assertTrue(request.equals(requestFor20170821));
	}
	
	@Test
	public void getNBPDataFromRest() {
		List<NBPData> nbpData = nbpRestConsummer.getRecentDataFromNBP();
		System.out.println(nbpData);
	}
	
	@Test
	public void parseDateTest() {
		NBPData nbpData = new NBPDataImpl();
		nbpData.setEffectiveDate("2017-08-22");
		Date parsedDate = nbpData.parseDate();
		Date expectedDate = getCalendarForDate(22, Calendar.AUGUST, 2017).getTime();
		logger.info(parsedDate);
		logger.info(expectedDate);
		assertTrue(parsedDate.equals(expectedDate));
	}
	
	@Test
	public void testQueryForSingleExchangeRate() {
		populateDBWithTestData();
		Date date = getCalendarForDate(25, Calendar.AUGUST, 2017).getTime();
		String code = "USD";
		ExchangeRate exchangeRate = dbService.getExchangeRate(date, code);
		assertTrue(exchangeRate.getRate() == 3.6112);
	}
	
	@Test
	public void testQueryForSingleCurrencyExchangeRates() {
		populateDBWithTestData();
		String code = "USD";
		List<ExchangeRate> exchangeRates = dbService.getExchangeRates(null, null, code);
		for (ExchangeRate e : exchangeRates) {
			logger.info(e);
		}
	}
	
	@Test
	public void testGetExchangeRateSeriesMap() {
		populateDBWithTestData();
		String code = "USD";
		Date startDate = getCalendarForDate(1, Calendar.AUGUST, 2017).getTime();
		Date endDate = getCalendarForDate(26, Calendar.AUGUST, 2017).getTime();
		
		Map<String, List<Double>> series = taskHandler.getExchangeRateSeriesModelForTimePeriod(startDate, endDate).getExchangeRateSeriesMap();
		List<Double> usdSeriesList = series.get(code);
		for (Double ers : usdSeriesList) {
			logger.info(ers);
		}
	}
	
	private Calendar getCalendarForDate(int day, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	private void populateDBWithTestData() {
		if(testData == null && !dbService.hasCurrentData()) {
			testData = nbpService.getRecentDataFromNBP();
			taskHandler.insertDataFromWeb(testData);
		}
	}
	
	private void populateDBWithBigTestData() {
		if(testData == null && !dbService.hasCurrentData()) {
			testData = nbpService.getRecentDataFromNBP();
			taskHandler.manageCoreTasks();
		}
	}
	
	@Test
	public void getAllDataFromNBP() {
		List<NBPData> allDataFromNBP = nbpRestConsummer.getAllDataFromNBP();
		allDataFromNBP.stream().forEach(e -> e.getRates().stream()
				.filter(r -> r.getCode().equals("USD"))
				.forEach(r -> System.out.println(e.getEffectiveDate() + " - " + r.getMid())));
	}
	
	@Test
	public void getAllCurrencyCodes() {
		populateDBWithTestData();
		ExchangeRateSeriesModel model  = taskHandler.getInitialExchangeRateSeriesModel();
		List<String> codes = model.getCurrencyCodes(); 
		codes.stream().forEach(e -> logger.info(e));
		assertTrue(codes.contains("EUR") && codes.contains("USD") && codes.contains("RUB"));
	}
	
	@Test
	public void getJsonDataPoints() {
		populateDBWithBigTestData();
		ExchangeRateSeriesModel model = taskHandler.getInitialExchangeRateSeriesModel();
		String jsonDataPoints = model.getJsonDataPointsMap().get("USD");
		logger.info(jsonDataPoints);
		String jsonDataPointsForLTL = model.getJsonDataPointsMap().get("LTL");
		logger.info(jsonDataPointsForLTL);
	}
	
	@Test
	public void test() {
		populateDBWithTestData();
		ExchangeRateSeriesModel model  = taskHandler.getInitialExchangeRateSeriesModel();
		List<Double> rateSeries = model.getExchangeRateSeriesMap().get("USD");
		Double[] rateSeriesArray = new Double[rateSeries.size()];
		rateSeriesArray = rateSeries.toArray(rateSeriesArray);
		
		double[] values = new double[rateSeriesArray.length];
		for (int i = 0; i < rateSeriesArray.length; i++) {
			values[i] = rateSeriesArray[i];
		}
		
		Vector rateVector = Vectors.dense(values);
		logger.info(rateVector);
		Vector forcst = ARIMA.autoFit(rateVector, 1, 1, 1).forecast(rateVector, 10);
		
		logger.info("forecast of next 10 observations: " + forcst);
	}
	
	@Test
	public void testEnhancementWithZeros() {
		populateDBWithBigTestData();
		List<ExchangeRate> litasSeries = taskHandler.getRatesForCurrencyCode("LTL");
		List<Date> dates = taskHandler.getRatesForCurrencyCode("USD").stream()
				.map(ExchangeRate::getDate)
				.collect(Collectors.toList());
		Collections.sort(dates);
		ExchangeRateSeriesModel model = taskHandler.getInitialExchangeRateSeriesModel();
		List<ExchangeRate> enhancedLitasSeries = model.getEnhancedWithZeros(litasSeries, dates);
		logger.info("Litas list size: " + litasSeries.size());
		logger.info("Dates list size: " + dates.size());
//		logger.info(enhancedLitasSeries);
		assertTrue(enhancedLitasSeries.size() == dates.size());
		assertTrue(enhancedLitasSeries.get(enhancedLitasSeries.size()-1).getRate() == 0);
//		logger.info(model.getExchangeRateSeriesMap().get("LTL"));
	}

}
