package pl.karolcyprowski.currat.spark;

import java.util.List;

import org.apache.spark.mllib.linalg.Vector;

public interface SparkTool {

	public Vector getForecast(int days, List<Double> rateSeries);
	
	public List<Double> getForecastList(int days, List<Double> rateSeries);
}
