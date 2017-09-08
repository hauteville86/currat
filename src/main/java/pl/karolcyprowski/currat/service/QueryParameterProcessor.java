package pl.karolcyprowski.currat.service;

import java.util.Calendar;
import java.util.Date;

public interface QueryParameterProcessor {

	public Date getStartDate (Calendar calendar);
	
	public Date getEndDate (Calendar calendar);
	
	public String getDefaultCode();
}
