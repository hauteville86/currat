package pl.karolcyprowski.currat.nbp;

import java.util.Calendar;

public interface NBPRequestBuilder {

	public String buildRequestUrl();
	
	public String buildPeriodRequestUrl(Calendar startDateCalendar, int periodLength);
}
