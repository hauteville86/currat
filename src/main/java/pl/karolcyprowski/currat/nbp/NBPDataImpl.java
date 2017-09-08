package pl.karolcyprowski.currat.nbp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NBPDataImpl implements NBPData {

	private String table;
	private String no;
	private String effectiveDate;
	private List<CurrencyRate> rates;
	
	public NBPDataImpl(){
		
	}
	
	public NBPDataImpl(Map<String, Object> source) {
		this.setTable(source.get("table").toString());
		this.setNo(source.get("no").toString());
		this.setEffectiveDate(source.get("effectiveDate").toString());
		List<CurrencyRate> currencyRates = parseCurrencyRates(source.get("rates"));
		this.setRates(currencyRates);
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public List<CurrencyRate> getRates() {
		return rates;
	}

	public void setRates(List<CurrencyRate> rates) {
		this.rates = rates;
	}
	
	public Date parseDate() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = format.parse(this.effectiveDate);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Date date = new Date();
			return date;
		}
	}
	
	private List<CurrencyRate> parseCurrencyRates (Object jsonRates) {
		List<CurrencyRate> currencyRates = new ArrayList<CurrencyRate>();
		if (jsonRates instanceof List) {
			List<Map> rates = (ArrayList)jsonRates;
			for (int i = 0; i < rates.size(); i++) {
				Map jsonRate = rates.get(i);
				CurrencyRate currencyRate = new CurrencyRateImpl(jsonRate);
				currencyRates.add(currencyRate);
			}
		}
		return currencyRates;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NBPDataImpl [table=");
		builder.append(table);
		builder.append(", no=");
		builder.append(no);
		builder.append(", effectiveDate=");
		builder.append(effectiveDate);
		builder.append(", rates=");
		builder.append(rates);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
}
