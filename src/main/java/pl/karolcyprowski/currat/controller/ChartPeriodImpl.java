package pl.karolcyprowski.currat.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChartPeriodImpl implements ChartPeriod {

	private Date startDate;
	private Date endDate;
	
	public ChartPeriodImpl(String startDateString, String endDateString, SimpleDateFormat dateFormatter) {
		try {
			startDate = dateFormatter.parse(startDateString);
			endDate = dateFormatter.parse(endDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

	public Date getStartDate() {
		return startDate;
	}


	public Date getEndDate() {
		return endDate;
	}

}
