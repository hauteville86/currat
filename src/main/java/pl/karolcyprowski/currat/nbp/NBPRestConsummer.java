package pl.karolcyprowski.currat.nbp;

import java.util.List;
import java.util.Map;

public interface NBPRestConsummer {

	public List<NBPData> getRecentDataFromNBP();

	public List<NBPData> getAllDataFromNBP();
}
