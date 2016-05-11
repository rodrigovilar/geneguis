package br.edu.ufcg.embedded.ise.geneguis.consolereader;

import org.openqa.selenium.WebDriver;


public interface BrowserConsoleReader {

	void readException(WebDriver webDriver, String url, String... msgs);

}
