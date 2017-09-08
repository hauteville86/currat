package pl.karolcyprowski.currat.nbp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NBPRestConsummerImpl implements NBPRestConsummer {
	
	@Autowired
	private NBPRequestBuilder nbpRequestBuilder;
	
	@Value("${pl.karolcyprowski.nbprequest.default_length}")
	private int defaultPeriodLength;
	
	private Calendar globalStartCalendar;
	
	public NBPRestConsummerImpl() {
		setGlobalStartCalendar();
	}

	@Override
	public List<NBPData> getRecentDataFromNBP() {
		RestTemplate restTemplate = new RestTemplate();
		String requestUrl = nbpRequestBuilder.buildRequestUrl();
		List<NBPData> nbpJSON = restTemplate.getForObject(requestUrl, NBPDataList.class);
		return nbpJSON;
	}
	
	public List<NBPData> getAllDataFromNBP() {
		//TODO: requestURL
		List<NBPData> allDataFromNBP = new ArrayList<NBPData>();
		RestTemplate restTemplate = new RestTemplate();
		Calendar startCalendar = globalStartCalendar;
		while (startCalendar.before(Calendar.getInstance())) {
			String requestUrl = nbpRequestBuilder.buildPeriodRequestUrl((Calendar)startCalendar.clone(), defaultPeriodLength);
			restTemplate.getForObject(requestUrl, NBPDataList.class).stream().forEach(e -> allDataFromNBP.add(e));
			startCalendar.add(Calendar.DATE, defaultPeriodLength + 1);
		}
		return allDataFromNBP;
	}
	
	private void setGlobalStartCalendar() {
		this.globalStartCalendar = Calendar.getInstance();
		this.globalStartCalendar.set(Calendar.YEAR, 2005);
		this.globalStartCalendar.set(Calendar.MONTH, Calendar.JANUARY);
		this.globalStartCalendar.set(Calendar.DAY_OF_MONTH, 1);
	}

}
