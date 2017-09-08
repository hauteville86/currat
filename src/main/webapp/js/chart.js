/**
 * 
 */

var timeSeriesAndCurrencyLabels = {};

var dateLabels = [];

var timeSeriesMap = {};

var myChart = null;

function renderChart() {
	var pointsArray = timeSeriesAndCurrencyLabels.timeSeries;
	var currencyLabels = timeSeriesAndCurrencyLabels.labels;
	console.log(pointsArray);
	var data = getData(pointsArray, dateLabels, currencyLabels);
	console.log(data);
	
	var chart = document.getElementById("myChart").getContext('2d');
	if (myChart != null) {
		myChart.destroy();
	}
	myChart = new Chart(chart, {
		type: 'line',
		data: data,
		options: {}
	});
}

function getData(pointsArray, dateLabels, currencyLabels) {
	var data = {};
	data.labels = dateLabels;
	data.datasets = [];
	for (var i = 0; i < pointsArray.length; i++) {
		var points = pointsArray[i];
		var datasetLabel = currencyLabels[i];
		var dataset = getDataset(points, datasetLabel);
		data.datasets.push(dataset);
	}
	return data;
	
}

function getDataset(points, datasetLabel) {
	var dataset = {};
	dataset.label = datasetLabel;
	dataset.data = [];
	dataset.fill = false;
	for(var i = 0; i < points.length; i++) {
		var singleDataPoint = points[i];
		dataset.data.push(singleDataPoint);
	}
	return dataset;
}

function getOptions(points) {
	var options = {
			elements: {
				line: {
					tension: 0
				}
			}
	};
	return options;
}

function initTimeSeriesAndCurrencyLabels() {
	var codesCheckboxList = document.getElementById("codesCheckboxList").children;
	timeSeriesAndCurrencyLabels.labels = [];
	timeSeriesAndCurrencyLabels.timeSeries = [];
	for (var i = 0; i < codesCheckboxList.length; i++) {
		var codeCheckboxElement = codesCheckboxList[i].children[0];
		var codeLabel = codesCheckboxList[i].children[1].textContent;
		if (codeLabel === 'CHF') {
			codeCheckboxElement.checked = true;
		}
		if (codeCheckboxElement.checked) {
			var timeSeriesAndCurrencyLabelsElement = {};
			timeSeriesAndCurrencyLabels.labels.push(codeLabel);
			var timeSeries = timeSeriesMap[codeLabel];
			timeSeriesAndCurrencyLabels.timeSeries.push(JSON.parse(timeSeries));
		}
	}
}

function addListenersToCheckboxElements() {
	var checkboxElements = document.getElementsByName("checkboxElement");
	for (var i = 0; i < checkboxElements.length; i++) {
		var checkboxElement = checkboxElements[i];
		var checkbox = checkboxElement.children[0];
		checkbox.onclick = function () {
			var label = this.parentElement.children[1].textContent;
			console.log(label + ": " + this.checked);
			if (this.checked) {
				addTimeSeries(label);
			} else {
				removeTimeSeries(label);
			}
			renderChart();
		}
	}
}

function addTimeSeries (label) {
	timeSeriesAndCurrencyLabels.labels.push(label);
	var timeSeries = timeSeriesMap[label];
	if (label === 'LTL') {
		console.log(timeSeries)
	}
	timeSeriesAndCurrencyLabels.timeSeries.push(JSON.parse(timeSeries));
}

function removeTimeSeries (label) {
	var indexOfLabel = timeSeriesAndCurrencyLabels.labels.indexOf(label);
	timeSeriesAndCurrencyLabels.labels.splice(indexOfLabel);
	timeSeriesAndCurrencyLabels.timeSeries.splice(indexOfLabel, 1);
	console.log(timeSeriesAndCurrencyLabels);
}

function setDateLabels(labels) {
	dateLabels = labels;
}

function setTimeSeriesMap(map) {
	timeSeriesMap = map;
}

function test() {
	initCurrencyCodesToShow();
}

