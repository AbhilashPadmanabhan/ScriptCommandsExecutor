package com.scriptexecutor.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scriptexecutor.beans.ScreenRegion;
import com.scriptexecutor.utils.JSONUtil;

public class ScreenDataLookupCache {

	private Map<String, List<ScreenRegion>> cache = new HashMap<String, List<ScreenRegion>>();
	private double widthFactor = 1.0;

	private double heightFactor = 1.0;
	
	private double contextArea = 50.0;

	public static ScreenDataLookupCache instance = null;

	static {
		instance = new ScreenDataLookupCache();
	}

	public static ScreenDataLookupCache getInstance() {
		return instance;
	}

	public void reloadCache(Map<String, Object> data) {
		cache.clear();
		Map<String, List<ScreenRegion>> textAnnotations = JSONUtil.getTextAnnotations(data);
		cache.putAll(textAnnotations);
	}

	public List<ScreenRegion> getRegionsFromTerm(String term) {
		return cache.get(term);
	}

	public void initializeScreenFactor(double width, double height) {
		widthFactor = width;
		heightFactor = height;
	}
	
	public double getWidthFactor() {
		return widthFactor;
	}

	public double getHeightFactor() {
		return heightFactor;
	}
	
	public double getContextArea() {
		return contextArea;
	}
}
