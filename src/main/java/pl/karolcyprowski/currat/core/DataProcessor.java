package pl.karolcyprowski.currat.core;

import java.util.List;

import pl.karolcyprowski.currat.entities.ExchangeRate;
import pl.karolcyprowski.currat.nbp.NBPData;

public interface DataProcessor {

	public List<ExchangeRate> parseNBPDataList(List<NBPData> nbpDataList);
	
}
