package pl.karolcyprowski.currat.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pl.karolcyprowski.currat.entities.ExchangeRate;
import pl.karolcyprowski.currat.nbp.NBPData;

public interface DBService {

	public boolean hasCurrentData();
	
	public void insertData(List<ExchangeRate> data);
	
	public ExchangeRate getExchangeRate(Date date, String code);
	
	public List<ExchangeRate> getExchangeRates(Date startDate, Date endDate, String code);
	
	public List<ExchangeRate> getExchangeRates(Date date);
	
	public List<ExchangeRate> getExchangeRates(String code);
}
