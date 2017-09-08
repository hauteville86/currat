package pl.karolcyprowski.currat.nbp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=CurrencyRateImpl.class)
public interface CurrencyRate {

	public String getCurrency();

	public void setCurrency(String currency);

	public String getCode();

	public void setCode(String code);

	public double getMid();

	public void setMid(double mid);
}
