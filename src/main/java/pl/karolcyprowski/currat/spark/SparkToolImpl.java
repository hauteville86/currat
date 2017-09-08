package pl.karolcyprowski.currat.spark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.springframework.stereotype.Component;

import com.cloudera.sparkts.models.ARIMA;
import com.cloudera.sparkts.models.ARIMAModel;

@Component
public class SparkToolImpl implements SparkTool {
	
	static Logger logger = Logger.getLogger(SparkToolImpl.class);
	
	public SparkToolImpl() {
		
	}

	public Vector getForecast(int days, List<Double> rateSeries) {
		Double[] rateSeriesArray = new Double[rateSeries.size()];
		rateSeriesArray = rateSeries.toArray(rateSeriesArray);
		
		double[] values = new double[rateSeriesArray.length];
		for (int i = 0; i < rateSeriesArray.length; i++) {
			values[i] = rateSeriesArray[i];
		}
		
		Vector rateVector = Vectors.dense(values);
		ARIMAModel model = ARIMA.autoFit(rateVector, 1, 1, 1);
		Vector forcst = model.forecast(rateVector, 1);
		for (int i = 0; i < days - 1; i++) {
			forcst = model.forecast(forcst, 1);
		}
//		Vector forcst = model.forecast(rateVector, days);
		return forcst;
	}
	
	public List<Double> getForecastList(int days, List<Double> rateSeries) {
		Vector vector = getForecast(days, rateSeries);
		double[] vectorArray = vector.toArray();
		List<Double> newRateSeries = new ArrayList<Double>();
		for (int i = rateSeries.size(); i < vector.size(); i++) {
			newRateSeries.add(vectorArray[i]);
		}
		return newRateSeries;
	}
}
