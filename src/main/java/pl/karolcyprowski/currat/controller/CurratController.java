package pl.karolcyprowski.currat.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.karolcyprowski.currat.core.ExchangeRateSeriesModel;
import pl.karolcyprowski.currat.core.TaskHandler;


@Controller
@RequestMapping("/")
public class CurratController {
	
	@Autowired
	private TaskHandler taskHandler;
	
	@Autowired
	private ControllerDataProvider controllerDataProvider;
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyy");
	
	
	@RequestMapping("/")
	public String getMainWindow(Model model, String startDate, String endDate) {
		taskHandler.manageCoreTasks();
		ExchangeRateSeriesModel exchangeRateSeriesModel = null;
		if (startDate == null && endDate == null) {
			exchangeRateSeriesModel = taskHandler.getInitialExchangeRateSeriesModel();
		} else {
			ChartPeriod chartPeriod = new ChartPeriodImpl(startDate, endDate, dateFormatter);
			exchangeRateSeriesModel = taskHandler.getExchangeRateSeriesModelForTimePeriod(chartPeriod.getStartDate(), chartPeriod.getEndDate()); 
		}
		List<String> codes = exchangeRateSeriesModel.getCurrencyCodes();
		model.addAttribute("codes", codes);
		Map<String, String> jsonDataPointsMap = exchangeRateSeriesModel.getJsonDataPointsMap();
		jsonDataPointsMap.entrySet().forEach(e -> model.addAttribute(e.getKey(), e.getValue()));
		model.addAttribute("timeSeries", jsonDataPointsMap);
		model.addAttribute("dateLabels", exchangeRateSeriesModel.getDataLabels());
		if (startDate == null) {
			startDate = dateFormatter.format(exchangeRateSeriesModel.getStartDate());
		}
		if (endDate == null) {
			endDate = dateFormatter.format(exchangeRateSeriesModel.getEndDate());
		}
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "init";
	}
}
