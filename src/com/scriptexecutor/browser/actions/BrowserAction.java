package com.scriptexecutor.browser.actions;

public interface BrowserAction {
	public void visitPage(String url);
	public void feedInput(String value) throws Exception;
	public void clickOnScreen(String text) throws Exception;
	public void launch(String appPath);
}
