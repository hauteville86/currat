package pl.karolcyprowski.currat.nbp;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=NBPDataImpl.class)
public interface NBPData {

	public String getTable();

	public void setTable(String table);

	public String getNo();

	public void setNo(String no);

	public String getEffectiveDate();

	public void setEffectiveDate(String effectiveDate);

	public List<CurrencyRate> getRates();

	public void setRates(List<CurrencyRate> rates);
	
	public Date parseDate();
}
