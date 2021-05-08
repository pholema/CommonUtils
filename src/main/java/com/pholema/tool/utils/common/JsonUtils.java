package com.pholema.tool.utils.common;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	public static String toString(JsonElement e) {
		return toString(e, null);
	}

	public static String toString(JsonElement e, String init) {
		if (e == null || e.isJsonNull()) {
			return init;
		} else {
			return e.getAsString();
		}
	}

	public static Double toDouble(JsonElement e) {
		return toDouble(e, 0.0);
	}

	public static Double toDouble(JsonElement e, Double init) {
		if (e == null || e.isJsonNull()) {
			return init;
		} else {
			return e.getAsDouble();
		}
	}

	public static Integer toInteger(JsonElement e) {
		return toInteger(e, 0);

	}

	public static Integer toInteger(JsonElement e, Integer init) {
		if (e == null || e.isJsonNull()) {
			return init;
		} else {
			return e.getAsInt();
		}
	}

	public static Long toLong(JsonElement e) {
		if (e == null || e.isJsonNull()) {
			return null;
		} else {
			return e.getAsLong();
		}
	}

	public static JsonArray jsonArraySorting(JsonArray rtnArray, final String sortingName) {
		return jsonArraySorting(rtnArray, sortingName, true);
	}

	// old: sorting(JsonArray arr,final String sortingKeyName)
	public static JsonArray jsonArraySorting(JsonArray rtnArray, final String sortingName, final boolean asc) {
		JsonArray sortedJsonArray = new JsonArray();
		List<JsonElement> jsonList = new ArrayList<>();
		for (JsonElement e : rtnArray) {
			jsonList.add(e);
		}
		Collections.sort(jsonList, new Comparator<JsonElement>() {
			@Override
			public int compare(JsonElement a, JsonElement b) {
				String valA = new String();
				String valB = new String();
				try {
					valA = a.getAsJsonObject().get(sortingName).getAsString();
					valB = b.getAsJsonObject().get(sortingName).getAsString();
				} catch (Exception e) {
					logger.error("continentSorting error:" + e.getMessage());
				}
				if (asc) {
					return valA.compareTo(valB);
				} else {
					return valB.compareTo(valA);
				}

			}
		});
		for (JsonElement e : jsonList) {
			sortedJsonArray.add(e);
		}
		return sortedJsonArray;
	}
	// Type mapType = new TypeToken<HashMap<String,String>>(){}.getType();

}
