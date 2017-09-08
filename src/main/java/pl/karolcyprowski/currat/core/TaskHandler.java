package pl.karolcyprowski.currat.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pl.karolcyprowski.currat.entities.ExchangeRate;
import pl.karolcyprowski.currat.nbp.NBPData;
import pl.karolcyprowski.currat.service.DBService;
import pl.karolcyprowski.currat.service.NBPService;

public interface TaskHandler {

	public DBService getDBService();
	
	public NBPService getNBPService();
	
	public void manageCoreTasks();
	
	public void insertDataFromWeb(List<NBPData> dataFromWeb);
	
	public ExchangeRateSeriesModel getExchangeRateSeriesModelForTimePeriod(Date startDate, Date endDate);
	
	public ExchangeRateSeriesModel getInitialExchangeRateSeriesModel();
	
	public void loadRecentDataFromWeb();
	
	public List<ExchangeRate> getRatesForCurrencyCode (String code);
	
	public void initForecasts();

}
