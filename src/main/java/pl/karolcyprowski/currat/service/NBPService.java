package pl.karolcyprowski.currat.service;

import java.util.List;
import java.util.Map;

import pl.karolcyprowski.currat.nbp.NBPData;
import pl.karolcyprowski.currat.nbp.NBPRestConsummer;

public interface NBPService {

	public List<NBPData> getRecentDataFromNBP();
	
	public NBPRestConsummer getNBPRestConsummer();

	public List<NBPData> getAllDataFromNBP();
	
	
}
