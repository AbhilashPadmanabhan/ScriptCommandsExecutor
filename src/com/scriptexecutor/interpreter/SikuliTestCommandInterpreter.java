package com.scriptexecutor.interpreter;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import com.scriptexecutor.browser.actions.BrowserAction;
import com.scriptexecutor.browser.actions.ChromeAction;
import com.scriptexecutor.cache.ScreenDataLookupCache;
import com.scriptexecutor.utils.FileUploadHelper;
import com.scriptexecutor.utils.JSONUtil;
import com.scriptexecutor.utils.KeywordHelper;

public class SikuliTestCommandInterpreter implements TestCommandInterpreter {

	private static ScreenDataLookupCache cache = ScreenDataLookupCache.getInstance();

	Screen screen = new Screen();
	List<String> storedScreenshots = new ArrayList<String>();
	BrowserAction action = null;

	int sleepTime = 0;

	public SikuliTestCommandInterpreter() {
		action = new ChromeAction(screen);
		sleepTime = 1000;
	}

	List<String> commands = null;

	@Override
	public void execute(String source) {
		try {
			Path filePath = Paths.get(source);

			Charset charset = StandardCharsets.UTF_8;
			commands = Files.readAllLines(filePath, charset);

			if (commands != null) {
				for (String command : commands) {
					if (command != null && command.length() > 0) {
						this.processCommand(command);
					}
				}
			} else {
				throw new Exception("No commands specified");
			}
		} catch (FindFailed e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processCommand(String command) throws Exception {

		if (command.toLowerCase().contains("browser")
				&& (command.toLowerCase().contains("launch") || command.toLowerCase().contains("open"))) {
			this.launchBrowser(command);
		} else if (command.toLowerCase().contains("url")) {
			this.openURL(command);
		} else if (command.toLowerCase().contains("enter")) {
			this.typeTextInput(command);
		} else if (command.toLowerCase().contains("click")) {
			this.clickObject(command);
		} else {
			throw new Exception("Command cannot be recognized");
		}
		Thread.sleep(sleepTime);

	}

	private void clickObject(String command) throws Exception {
		action.clickOnScreen(KeywordHelper.getTextToLookUp(command));
	}

	private void typeTextInput(String command) throws Exception {
		String[] field = KeywordHelper.getFieldNameAndValue(command);
		action.clickOnScreen(field[0]);
		action.feedInput(field[1]);
	}

	private void openURL(String command) {
		String url = KeywordHelper.getURL(command);
		action.visitPage(url);
		this.signalPageLoading();
	}

	private void signalPageLoading() {
		String fileName = System.getProperty("user.dir") + "/images/test.png";
		// this.saveImageScreenshot(fileName);

		BufferedImage bufImage = null;
		try {
			bufImage = ImageIO.read(new File(System.getProperty("user.dir") + "/images/pro.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		double imageWidth = bufImage.getWidth();
		double imageHeight = bufImage.getHeight();

		Rectangle rect = screen.getBounds();
		double widthDiffFactor = (rect.getWidth() / imageWidth);
		double heightDiffFactor = (rect.getHeight() / imageHeight);

		cache.initializeScreenFactor(widthDiffFactor, heightDiffFactor);
		String responseData = null;
		
		try {
			responseData = String.join("",
					Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/src/apivision.json")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.refreshImageDataFromAPI(responseData);
	}

	private void refreshImageDataFromAPI(String responseData) {
		Map<String, Object> responseMap = JSONUtil.jsonToMap(responseData);
		cache.reloadCache(responseMap);
	}

	private void launchBrowser(String command) {
		String browser = KeywordHelper.getBrowserName(command);
		action.launch("/Applications/" + browser + ".app");
	}

	private void saveImageScreenshot(String fileName) {
		BufferedImage bufImage;

		try {
			bufImage = screen.capture().getImage();
			double imageWidth = bufImage.getWidth();
			double imageHeight = bufImage.getHeight();

			Rectangle rect = screen.getBounds();
			double widthDiffFactor = (imageWidth / rect.getWidth());
			double heightDiffFactor = (imageHeight / rect.getHeight());

			cache.initializeScreenFactor(widthDiffFactor, heightDiffFactor);

			File file1 = new File(fileName);
			file1.getParentFile().mkdirs();
			ImageIO.write(bufImage, "png", file1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
