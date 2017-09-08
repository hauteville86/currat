package pl.karolcyprowski.currat.dao;

import java.util.Date;
import java.util.List;

import pl.karolcyprowski.currat.entities.ExchangeRate;
import pl.karolcyprowski.currat.nbp.NBPData;

public interface NBPDataDAO {
	

	public void insertData(List<ExchangeRate> exchangeRates);
	
	public ExchangeRate getExchangeRate(Date date, String code);
	
	public List<ExchangeRate> getExchangeRates(Date startDate, Date endDate, String code);
	
	public List<ExchangeRate> getExchangeRates(Date date);
	
	public List<ExchangeRate> getExchangeRates(Date startDate, Date endDate);
	
	public List<ExchangeRate> getExchangeRates(String code);
}
