package br.edu.ufcg.embedded.ise.geneguis.consolereader;

import java.io.IOException;
import java.util.List;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class FirefoxConsoleReader implements BrowserConsoleReader {

	@Override
	public void readException(WebDriver webDriver, String url, String... msgs) {
		try {
			FirefoxProfile ffProfile = new FirefoxProfile();
			JavaScriptError.addExtension(ffProfile);
			final WebDriver driver = new FirefoxDriver(ffProfile);

			driver.get(url);
			final List<JavaScriptError> jsErrors = JavaScriptError
					.readErrors(driver);
			for (int i = 0; i < msgs.length; i++) {
				Assert.assertEquals(msgs[i], jsErrors.get(i).getConsole());
//				Assert.assertEquals(msgs[i], jsErrors.get(i).getErrorMessage());
			}

		} catch (IOException e) {
			Assert.fail();
		}

	}

}
