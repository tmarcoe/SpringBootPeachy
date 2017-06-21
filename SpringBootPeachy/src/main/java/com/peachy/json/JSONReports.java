package com.peachy.json;

import java.math.BigInteger;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONReports {
	
	
	public JSONObject JSONCreateSalesReport(List<Double> totals, String reportTitle) {		
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray datasets = new JSONArray();
		JSONObject dataset = new JSONObject();
		String[] labels = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		JSONObject options = new JSONObject();
		JSONObject scales = new JSONObject();
		JSONArray yAxes = new JSONArray();
		JSONObject yObj = new JSONObject();
		JSONObject ticks = new JSONObject();
		
		json.put("type", "line");
		json.put("data", data);
		data.put("labels", labels);
		data.put("datasets", datasets);
		datasets.put(dataset);
		dataset.put("label", reportTitle );
		dataset.put("fill", false);
		dataset.put("backgroundColor", "rgba(100, 0, 128, 1.0)");
		dataset.put("data", totals.toArray());
		dataset.put("borderWidth", 2);
		json.put("options", options);
		options.put("scales", scales);
		options.put("responsive", false);
		options.put("display", true);
		options.put("text", reportTitle);
		scales.put("yAxes", yAxes);
		yObj.put("ticks", ticks);
		ticks.put("beginAtZero", true);
		yAxes.put(yObj);
		
		return json;
	}
	
	public JSONObject JSONCreateGenderReport(List<Double> genders, String heading) {
		String[] bgColors = {"rgba(200, 10, 110, 0.7)", "rgba(50, 0, 200, 0.7)"};
		Object[] gpercent = genders.toArray();
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray datasets = new JSONArray();
		JSONObject d1 = new JSONObject();
		String[] labels = {String.format("Female (%.2f%%)", (double) gpercent[0]),String.format("Male (%.2f%%)", (double) gpercent[1])};
		JSONObject options = new JSONObject();
		
		json.put("type", "pie");
		json.put("data", data);
		data.put("labels", labels);
		data.put("datasets", datasets);
		datasets.put(d1);
		d1.put("label", heading );
		d1.put("backgroundColor", bgColors);
		d1.put("data", genders.toArray());
		json.put("options", options);
		options.put("responsive", false);
		options.put("display", true);
		options.put("text", heading);
		return json;
	}
	
	public JSONObject JSONCreateServeyReport(List<Double> answers, String heading) {
		String[] bgColors = {"rgba(200, 0, 0, 0.7)", "rgba(0, 200, 200, 0.7)","rgba(0, 0, 200, 0.7)"};
		Object[] yData = answers.toArray();
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray datasets = new JSONArray();
		JSONObject d1 = new JSONObject();
		JSONObject options = new JSONObject();
		String[] labels = {"Satisfaction", "Navigation", "Prices"};
		
		json.put("type", "bar");
		json.put("data", data);
		data.put("labels", labels);
		data.put("datasets", datasets);
		datasets.put(d1);
		d1.put("label", heading);
		d1.put("backgroundColor", bgColors);
		d1.put("data", yData);
		json.put("options", options);
		options.put("responsive", false);
		options.put("display", true);
		options.put("text", heading);

		
		return json;
	}
	
	public JSONObject JSONCreateCustomerCount(List<BigInteger> customers, String heading) {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray datasets = new JSONArray();
		JSONObject dataset = new JSONObject();
		String[] labels = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		JSONObject options = new JSONObject();
		JSONObject scales = new JSONObject();
		JSONArray yAxes = new JSONArray();
		JSONObject yObj = new JSONObject();
		JSONObject ticks = new JSONObject();
		
		json.put("type", "line");
		json.put("data", data);
		data.put("labels", labels);
		data.put("datasets", datasets);
		datasets.put(dataset);
		dataset.put("label", heading );
		dataset.put("fill", false);
		dataset.put("backgroundColor", "rgba(100, 0, 128, 1.0)");
		dataset.put("data", customers.toArray());
		dataset.put("borderWidth", 2);
		json.put("options", options);
		options.put("scales", scales);
		options.put("responsive", false);
		options.put("display", true);
		options.put("text", heading);
		scales.put("yAxes", yAxes);
		yObj.put("ticks", ticks);
		ticks.put("beginAtZero", true);
		yAxes.put(yObj);
		
		return json;
		
	}

	public JSONObject JSONCreateCogsReport( List<Double> cogs, String reportTitle) {		
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray datasets = new JSONArray();
		JSONObject d1 = new JSONObject();
		String[] labels = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		JSONObject options = new JSONObject();
		JSONObject scales = new JSONObject();
		JSONArray yAxes = new JSONArray();
		JSONObject yObj = new JSONObject();
		JSONObject ticks = new JSONObject();
		
		json.put("type", "line");
		json.put("data", data);
		data.put("labels", labels);
		data.put("datasets", datasets);
		datasets.put(d1);
		d1.put("label", reportTitle );
		d1.put("fill", false);
		d1.put("backgroundColor", "rgba(100, 0, 128, 1.0)");
		d1.put("data", cogs.toArray());
		d1.put("borderWidth", 2);

		json.put("options", options);
		options.put("scales", scales);
		options.put("responsive", false);
		options.put("display", true);
		options.put("text", reportTitle);
		scales.put("yAxes", yAxes);
		yObj.put("ticks", ticks);
		ticks.put("beginAtZero", true);
		yAxes.put(yObj);
		
		return json;
	}

}
