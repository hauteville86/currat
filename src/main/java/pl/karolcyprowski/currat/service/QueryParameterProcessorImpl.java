package pl.karolcyprowski.currat.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

public class QueryParameterProcessorImpl implements QueryParameterProcessor{

	@Value("${pl.karolcyprowski.query.default_code}")
	private String defaultCode;
	
	public Date getStartDate (Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public Date getEndDate (Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	public String getDefaultCode() {
		return defaultCode;
	}
}
