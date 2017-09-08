package pl.karolcyprowski.currat.nbp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;

public class NBPRequestBuilderImpl implements NBPRequestBuilder {
	
	@Value("${pl.karolcyprowski.nbprequest.default_length}")
	private int defaultPeriodLength;

	@Override
	public String buildRequestUrl() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, defaultPeriodLength * (-1));
		return buildPeriodRequestUrl(calendar, defaultPeriodLength);
	}

	@Override
	public String buildPeriodRequestUrl(Calendar startDateCalendar, int periodLength) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String startDateString = format.format(startDateCalendar.getTime());
		startDateCalendar.add(Calendar.DATE, periodLength);
		if(startDateCalendar.after(Calendar.getInstance())) {
			startDateCalendar = Calendar.getInstance();
		}
		String endDateString = format.format(startDateCalendar.getTime());
		return buildRequestUrl(startDateString, endDateString);
	}
	
	private String buildRequestUrl (String startDateString, String endDateString) {
		StringBuilder builder = new StringBuilder();
		builder.append("http://api.nbp.pl/api/exchangerates/tables/A/");
		builder.append(startDateString);
		builder.append("/");
		builder.append(endDateString);
		builder.append("/?format=json");
		return builder.toString();
	}

}
