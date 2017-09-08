package pl.karolcyprowski.currat.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pl.karolcyprowski.currat.entities.ExchangeRate;


public interface ExchangeRateSeriesModel {

	public Date getStartDate();
	
	public Date getEndDate();
	
	public Map<String, List<Double>> getExchangeRateSeriesMap();
	
	public List<String> getDataLabels();
	
	public List<String> getCurrencyCodes();
	
	public Map<String, String> getJsonDataPointsMap();
	
	public List<ExchangeRate> getEnhancedWithZeros(List<ExchangeRate> list, List<Date> dates);
	
	public void setForecasts(Map<String, List<Double>> forecasts);
}
