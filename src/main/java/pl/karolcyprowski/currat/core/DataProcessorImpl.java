package pl.karolcyprowski.currat.core;

import java.util.ArrayList;
import java.util.List;

import pl.karolcyprowski.currat.entities.ExchangeRate;
import pl.karolcyprowski.currat.nbp.CurrencyRate;
import pl.karolcyprowski.currat.nbp.NBPData;

public class DataProcessorImpl implements DataProcessor {

	@Override
	public List<ExchangeRate> parseNBPDataList(List<NBPData> nbpDataList) {
		List<ExchangeRate> exchangeRates = new ArrayList<ExchangeRate>();
		for (int i = 0; i < nbpDataList.size(); i++) {
			NBPData nbpData = nbpDataList.get(i);
			List<CurrencyRate> currencyRates = nbpData.getRates();
			for (int j = 0; j < currencyRates.size(); j++) {
				CurrencyRate currencyRate = currencyRates.get(j);
				ExchangeRate exchangeRate = new ExchangeRate();
				exchangeRate.setDate(nbpData.parseDate());
				exchangeRate.setCurrencyCode(currencyRate.getCode());
				exchangeRate.setRate(currencyRate.getMid());
				exchangeRates.add(exchangeRate);
			}
		}
		return exchangeRates;
	}
	

}
