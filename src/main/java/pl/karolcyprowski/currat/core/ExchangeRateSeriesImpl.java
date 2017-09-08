package pl.karolcyprowski.currat.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRateSeriesImpl implements ExchangeRateSeries {
	
	private Date date;
	
	private double rate;
	
	public ExchangeRateSeriesImpl() {
		
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof ExchangeRateSeries) {
			return this.date.compareTo(((ExchangeRateSeries)o).getDate());
		}
		return 0;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public double getRate() {
		return this.rate;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	public Map<Object, Object> getAsMap() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("x", this.date);
//		map.put("x", this.date.getTime());
		map.put("y", this.rate);
		return map;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExchangeRateSeriesImpl [date=");
		builder.append(date);
		builder.append(", rate=");
		builder.append(rate);
		builder.append("]");
		return builder.toString();
	}
	
	

	
	
}
