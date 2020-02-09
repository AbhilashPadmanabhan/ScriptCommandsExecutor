package com.scriptexecutor.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scriptexecutor.beans.ScreenPoint;
import com.scriptexecutor.beans.ScreenRegion;
import com.scriptexecutor.cache.ScreenDataLookupCache;

public class PointSelectionHelper {

	private static int maxX(ScreenPoint... p) {
		int max = p[0].getX();
		for (int i = 1; i < p.length; i++) {
			if (p[i].getX() > max) max = p[i].getX();
		}
		return max;
	}

	private static int minX(ScreenPoint... p) {
		int min = p[0].getX();
		for (int i = 1; i < p.length; i++) {
			if (p[i].getX() < min) min = p[i].getX();
		}
		return min;
	}

	private static int maxY(ScreenPoint... p) {
		int max = p[0].getY();
		for (int i = 1; i < p.length; i++) {
			if (p[i].getY() > max) max = p[i].getY();
		}
		return max;
	}

	private static int minY(ScreenPoint... p) {
		int min = p[0].getY();
		for (int i = 1; i < p.length; i++) {
			if (p[i].getY() < min) min = p[i].getY();
		}
		return min;
	}

	
//	private static ScreenPoint max(ScreenPoint b1, ScreenPoint b2) {
//		return (b1.getX() > b2.getX()) ? b1 : b2;
//	}
//
//	private static ScreenPoint min(ScreenPoint a1, ScreenPoint a2) {
//		return (a1.getX() < a2.getX()) ? a1 : a2;
//	}
	private static boolean isHorizontallyLinearRegions(ScreenRegion regionOne, ScreenRegion regionTwo) {
		return regionOne.getA().getY() == regionTwo.getA().getY() && regionOne.getB().getY() == regionTwo.getB().getY()
				&& regionOne.getC().getY() == regionTwo.getC().getY()
				&& regionOne.getD().getY() == regionTwo.getD().getY();
	}

	public static ScreenPoint calculateRandomPointFromRegion(ScreenRegion f) {

		ScreenPoint newPoint = null;

		if (f != null) {
			ScreenPoint a = f.getA();
			ScreenPoint b = f.getB();
			ScreenPoint d = f.getD();

			newPoint = new ScreenPoint(a.getX() + (b.getX() - a.getX()) * 3 / 4,
					a.getY() + (d.getY() - a.getY()) * 3 / 4);
		}
		return newPoint;
	}

	public static ScreenRegion mergeRegions(ScreenRegion regionOne, ScreenRegion regionTwo) {

		ScreenPoint pointA = new ScreenPoint(minX(new ScreenPoint[] {regionOne.getA(), regionTwo.getA()}), 
										minY(new ScreenPoint[] {regionOne.getA(), regionTwo.getA()}));
		ScreenPoint pointB = new ScreenPoint(maxX(new ScreenPoint[] {regionOne.getB(), regionTwo.getB()}),
										minY(new ScreenPoint[] {regionOne.getB(), regionOne.getB()}));
		ScreenPoint pointC = new ScreenPoint(maxX(new ScreenPoint[] {regionOne.getC(), regionTwo.getC()}),
										maxY(new ScreenPoint[] {regionOne.getC(), regionTwo.getC()}));
		ScreenPoint pointD = new ScreenPoint(minX(new ScreenPoint[] {regionOne.getD(), regionTwo.getD()}),
										maxY(new ScreenPoint[] {regionOne.getD(), regionTwo.getD()}));
//		ScreenPoint pointA = min(regionOne.getA(), regionTwo.getA());
//		ScreenPoint pointB = max(regionOne.getB(), regionTwo.getB());
//		ScreenPoint pointC = max(regionOne.getC(), regionTwo.getC());
//		ScreenPoint pointD = min(regionOne.getD(), regionTwo.getD());

		ScreenRegion commonRegion = new ScreenRegion(pointA, pointB, pointC, pointD);

		return commonRegion;
	}

	public static ScreenRegion determineRegion(String keyword) {
		return determineRegion(keyword, null);
	}

	public static ScreenRegion determineRegion(String keyword, ScreenRegion contextualRegion) {

		ScreenRegion reg = null;

		Map<String, List<ScreenRegion>> keywordsRegions = new HashMap<>();
		List<String> tokens = Arrays.asList(keyword.split(" "));

		for (String token : tokens) {
			List<ScreenRegion> regions = ScreenDataLookupCache.getInstance().getRegionsFromTerm(token);
			keywordsRegions.put(token, regions);
		}

		boolean singleRegionDetected = false;

		for (int i = 0; i < tokens.size(); i++) {
			List<ScreenRegion> regionsA = keywordsRegions.get(tokens.get(i));

			if (regionsA.size() == 1) {
				singleRegionDetected = true;
				reg = regionsA.get(0);
				break;
			}
		}

		if (!singleRegionDetected) {

			List<ScreenRegion> tentativeRegionsList = new ArrayList<>();

			for (int i = 0; i < tokens.size(); i++) {
				for (int j = i + 1; i < tokens.size(); i++) {
					List<ScreenRegion> regionsA = keywordsRegions.get(tokens.get(i));
					List<ScreenRegion> regionsB = keywordsRegions.get(tokens.get(j));

					for (ScreenRegion regionA : regionsA) {
						for (ScreenRegion regionB : regionsB) {
							// horizontally linear regions
							if (isHorizontallyLinearRegions(regionA, regionB)) {
								tentativeRegionsList.add(mergeRegions(regionA, regionB));
							}
						}
					}
				}
			}

			ScreenRegion[] tentativeRegions = new ScreenRegion[tentativeRegionsList.size()];
			tentativeRegions = tentativeRegionsList.toArray(tentativeRegions);
			if (contextualRegion != null) {
				reg = nearestRegion(contextualRegion, tentativeRegions);
			}

		}

		return reg;
	}

	public static ScreenPoint makeRelative(ScreenPoint pt) {
		ScreenDataLookupCache cache = ScreenDataLookupCache.getInstance();
		pt.setX((int) (pt.getX() * cache.getWidthFactor()));
		pt.setY((int) (pt.getY() * cache.getHeightFactor()));
		return pt;
	}

	public static ScreenRegion nearestRegion(ScreenRegion overall, ScreenRegion... regions) {
		// List<ScreenPoint> regionCentres = new ArrayList<>();
		double leastDistance = -1;
		int indexLeastDistance = -1;

		ScreenPoint focussedCentre = getCentre(overall);
		for (int i = 0; i < regions.length; i++) {
			ScreenRegion region = regions[i];
			// regionCentres.add(getCentre(region));
			ScreenPoint regionCentre = getCentre(region);
			double distance = Math.sqrt(Math.pow(regionCentre.getX() - focussedCentre.getX(), 2.0)
					+ Math.pow(regionCentre.getY() - focussedCentre.getY(), 2.0));
			if (indexLeastDistance < 0) {
				leastDistance = distance;
				indexLeastDistance = i;
			}
			else if (distance < leastDistance) {
				leastDistance = distance;
				indexLeastDistance = i;
			}
		}

		return (indexLeastDistance >= 0 && indexLeastDistance < regions.length - 1) ? regions[indexLeastDistance] : null;
	}

	public static ScreenPoint getCentre(ScreenRegion region) {
		return new ScreenPoint((int) ((region.getA().getX() + region.getC().getX()) / 2.0),
				(int) ((region.getA().getY() + region.getC().getY()) / 2.0));
	}
};