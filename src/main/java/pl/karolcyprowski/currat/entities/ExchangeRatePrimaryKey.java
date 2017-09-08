package pl.karolcyprowski.currat.entities;

import java.io.Serializable;
import java.util.Date;

public class ExchangeRatePrimaryKey implements Serializable{

	private String currencyCode;
	
	private Date date;
	
	public ExchangeRatePrimaryKey(){
		
	}
	
	public ExchangeRatePrimaryKey(String currencyCode, Date date) {
		this.currencyCode = currencyCode;
		this.date = date;
	}
}
