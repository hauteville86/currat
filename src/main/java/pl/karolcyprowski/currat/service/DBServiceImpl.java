package pl.karolcyprowski.currat.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.karolcyprowski.currat.dao.NBPDataDAO;
import pl.karolcyprowski.currat.entities.ExchangeRate;

@Service
public class DBServiceImpl implements DBService {
	
	@Autowired
	private NBPDataDAO nbpDataDAO;
	
	@Autowired
	private QueryParameterProcessor queryParameterProcessor;

	public boolean hasCurrentData() {
		Date date = queryParameterProcessor.getStartDate(Calendar.getInstance());
		String code = queryParameterProcessor.getDefaultCode();
		ExchangeRate sampleCurrentExchangeRate = getExchangeRate(date, code);
		return sampleCurrentExchangeRate != null;
	}
	
	@Transactional
	public void insertData(List<ExchangeRate> data){
		nbpDataDAO.insertData(data);		
	}
	
	@Transactional
	public ExchangeRate getExchangeRate(Date date, String code) {
		return nbpDataDAO.getExchangeRate(date, code);
	}
	
	@Transactional
	public List<ExchangeRate> getExchangeRates(Date startDate, Date endDate, String code) {
		if (code != null) {
			if (startDate != null && endDate != null) {
				return nbpDataDAO.getExchangeRates(startDate, endDate, code);
			}
			return nbpDataDAO.getExchangeRates(code);
		} else if (startDate != null && endDate != null) {
			return nbpDataDAO.getExchangeRates(startDate, endDate);
		} else {
			return new ArrayList<ExchangeRate>();
		}	
	}
	
	@Transactional
	public List<ExchangeRate> getExchangeRates(Date date) {
		return nbpDataDAO.getExchangeRates(date);
	}
	
	@Transactional
	public List<ExchangeRate> getExchangeRates(String code) {
		return nbpDataDAO.getExchangeRates(code);
	}

}
