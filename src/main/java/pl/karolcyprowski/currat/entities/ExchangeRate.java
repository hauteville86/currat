package pl.karolcyprowski.currat.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@IdClass(ExchangeRatePrimaryKey.class)
@Table(name="exchange_rate")
public class ExchangeRate implements Comparable<ExchangeRate>{

	@Id
	@Column(name="currency_code")
	private String currencyCode;
	
	@Id
	@Column(name="date", columnDefinition="TIMESTAMP")
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Column(name="rate")
	private double rate;
	
	public ExchangeRate() {
		
	}
	
	public ExchangeRate(String currencyCode, Date date, double rate) {
		this.currencyCode = currencyCode;
		this.date = date;
		this.rate = rate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExchangeRate [currencyCode=");
		builder.append(currencyCode);
		builder.append(", date=");
		builder.append(date);
		builder.append(", rate=");
		builder.append(rate);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(ExchangeRate o) {
		return this.getDate().compareTo(o.getDate());
	}
	
}
