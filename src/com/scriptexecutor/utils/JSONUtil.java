package com.scriptexecutor.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptexecutor.beans.ScreenPoint;
import com.scriptexecutor.beans.ScreenRegion;

public class JSONUtil {

	public static Map<String, Object> jsonToMap(String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();

		try {
			map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	public static Map<String, List<ScreenRegion>> getTextAnnotations(Map<String, Object> data) {

		Map<String, List<ScreenRegion>> map = new HashMap<String, List<ScreenRegion>>();

		if (data instanceof Map<?, ?>) {
			Map<?, ?> dataVal = (Map<?, ?>) ((List<?>) data.get("data")).get(0);
			List<?> terms = null;
			if (dataVal instanceof Map<?, ?>) {
				terms = (ArrayList<?>) dataVal.get("textAnnotations");
				for (Object termData : terms) {
					if (termData instanceof Map<?, ?>) {
						Map<?, ?> values = (Map<?, ?>) termData;
						String term = (String) values.get("description");

						Map<?, ?> pos = (Map<?, ?>) values.get("boundingPoly");

						List<?> positions = (List<?>) pos.get("vertices");
						Map<?, ?> pointA = (Map<?, ?>) positions.get(0);
						Map<?, ?> pointB = (Map<?, ?>) positions.get(1);
						Map<?, ?> pointC = (Map<?, ?>) positions.get(2);
						Map<?, ?> pointD = (Map<?, ?>) positions.get(3);
						
						ScreenPoint ptA = PointSelectionHelper.makeRelative(new ScreenPoint((int) pointA.get("x"), (int) pointA.get("y")));
						ScreenPoint ptB = PointSelectionHelper.makeRelative(new ScreenPoint((int) pointB.get("x"), (int) pointB.get("y")));
						ScreenPoint ptC = PointSelectionHelper.makeRelative(new ScreenPoint((int) pointC.get("x"), (int) pointC.get("y")));
						ScreenPoint ptD = PointSelectionHelper.makeRelative(new ScreenPoint((int) pointD.get("x"), (int) pointD.get("y")));
						
						ScreenRegion region = new ScreenRegion(ptA, ptB, ptC, ptD);

						List<ScreenRegion> listOfRegions = null;
						if (map.get(term) != null) {
							listOfRegions = map.get(term);
						} else {
							listOfRegions = new ArrayList<ScreenRegion>();
						}

						listOfRegions.add(region);
						
						map.put(term, listOfRegions);
					}
				}
			}
		}

		return map;

	}
}