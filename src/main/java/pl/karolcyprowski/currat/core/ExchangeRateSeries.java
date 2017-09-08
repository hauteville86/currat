package pl.karolcyprowski.currat.core;

import java.util.Date;
import java.util.Map;

public interface ExchangeRateSeries extends Comparable{

	public Date getDate();
	
	public double getRate();
	
	public void setDate(Date date);
	
	public void setRate(double rate);
	
	public Map<Object, Object> getAsMap();
}
