package com.scriptexecutor.browser.actions;

import org.sikuli.script.App;
import org.sikuli.script.Key;
import org.sikuli.script.Location;
import org.sikuli.script.Screen;

import com.scriptexecutor.beans.ScreenPoint;
import com.scriptexecutor.beans.ScreenRegion;
import com.scriptexecutor.utils.PointSelectionHelper;

public class ChromeAction implements BrowserAction {

	Screen screen = null;

	ScreenRegion overallRegion = null;

	public ChromeAction(Screen screen) {
		this.screen = screen;
	}

	@Override
	public void visitPage(String url) {
		screen.type(Key.CMD, "L");
		screen.type(url);
		screen.type(Key.ENTER);
	}

	@Override
	public void feedInput(String value) throws Exception {
		screen.type(value);
	}

	@Override
	public void clickOnScreen(String text) throws Exception {
		ScreenRegion region = PointSelectionHelper.determineRegion(text, overallRegion);
		ScreenPoint point = PointSelectionHelper.calculateRandomPointFromRegion(region);

		screen.click(new Location(point.getX(), point.getY()));

		if (overallRegion == null)
			overallRegion = region;
		else
			overallRegion = PointSelectionHelper.mergeRegions(overallRegion, region);
	}

	@Override
	public void launch(String appPath) {
		App.open(appPath);
	}

}
