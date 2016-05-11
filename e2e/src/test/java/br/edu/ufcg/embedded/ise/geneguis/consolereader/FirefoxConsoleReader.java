package br.edu.ufcg.embedded.ise.geneguis.consolereader;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import br.edu.ufcg.embedded.ise.geneguis.WebBrowserTestCase;
import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

public class FirefoxConsoleReader implements BrowserConsoleReader {

	@Override
	public void readException(WebDriver webDriver, String msg) {
		final List<JavaScriptError> jsErrors = JavaScriptError
				.readErrors(WebBrowserTestCase.driver);
		
		boolean found = false;
		for (JavaScriptError javaScriptError : jsErrors) {
			if (javaScriptError.getErrorMessage() != null && javaScriptError.getErrorMessage().contains(msg)) {
				found = true;
			}
		}
		
		Assert.assertTrue("(" + msg + ") not found", found);
	}

}
