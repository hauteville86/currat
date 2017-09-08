/**
 * 
 */

function getDatasets(points, label) {
	var chartInputData = {};
	chartInputData.datasets = [];
	var dataset = {};
	dataset.label = label;
	dataset.data = [];
	chartInputData.labels = [];
	for (i = 0; i < points.length; i++) {
		var singleDataPoint = points[i];
		chartInputData.labels.push(singleDataPoint.x);
//		chartInputData.datasets.push(new Number(singleDataPoint.y));
		dataset.data.push(singleDataPoint.y);
//		chartInputData.datasets.push(singleDataPoint.y);
	}
	chartInputData.datasets.push(dataset);
	console.log(chartInputData);
	return chartInputData;
}

function getNumberObject(singleDataPoint) {
	var number = new Number(singleDataPoint.y);
	number.data = [];
	number.data.push(singleDataPoint.y);
	return number;
}