package pl.karolcyprowski.currat.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.karolcyprowski.currat.nbp.NBPData;
import pl.karolcyprowski.currat.nbp.NBPRestConsummer;

@Service
public class NBPServiceImpl implements NBPService {
	
	@Autowired
	private NBPRestConsummer nbpRestConsummer;

	public List<NBPData> getRecentDataFromNBP() {
		return nbpRestConsummer.getRecentDataFromNBP();
	}
	
	public NBPRestConsummer getNBPRestConsummer(){
		return nbpRestConsummer;
	}
	
	public List<NBPData> getAllDataFromNBP() {
		return nbpRestConsummer.getAllDataFromNBP();
	}

}
