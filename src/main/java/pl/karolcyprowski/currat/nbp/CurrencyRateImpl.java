package pl.karolcyprowski.currat.nbp;

import java.util.Map;

public class CurrencyRateImpl implements CurrencyRate{

	private String currency;
	private String code;
	private double mid;
	
	public CurrencyRateImpl() {
		
	}
	
	public CurrencyRateImpl(Map<String, Object> source) {
		this.currency = source.get("currency").toString();
		this.code = source.get("code").toString();
		this.mid = Double.parseDouble(source.get("mid").toString());
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getMid() {
		return mid;
	}

	public void setMid(double mid) {
		this.mid = mid;
	}
}
