package br.edu.ufcg.embedded.ise.geneguis;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.openqa.selenium.By;

import com.google.gson.Gson;

import br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint;
import br.edu.ufcg.embedded.ise.geneguis.backend.WidgetType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.EntityTypeDeployRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.RuleRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.WidgetRest;

public class Helper {

	static final String SERVER_URL = "http://localhost:8080/";
	
	static void openApp() {
		EntityWidgetTest.driver.get(SERVER_URL);
	}

	static void clickEntityType(Class<?> entityType) {
		EntityWidgetTest.driver.findElement(By.id("entityType_" + entityType.getSimpleName())).click();
	}

	static void checkTitle(Class<?> entityType) {
		EntityWidgetTest.driver.findElement(By.id("title_" + entityType.getSimpleName()));
	}

	static void checkList(Class<?> entityType) {
		EntityWidgetTest.driver.findElement(By.id("list_" + entityType.getSimpleName()));
	}

	static void widget(String name, WidgetType type, PortRest... ports) {
		WidgetRest widget = new WidgetRest();
		widget.setName(name);
		widget.setType(type.name());
		widget.setRequiredPorts(Arrays.asList(ports));
		
		postJSON(SERVER_URL + "widgets", widget);
		
		post(SERVER_URL + "widgets/" + name + "/code", readWidgetFile(name));
	}

	static String readWidgetFile(String fileName) {
		URL resource = EntryPoint.class.getResource("/widgets/" + fileName + ".js");
		String filePath = resource.getPath();
		try {
			Path widgetPath = Paths.get(filePath);
			return new String(Files.readAllBytes(widgetPath));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
			return null;
		}
	}
	
	static void rule(String port, String widget) {
		RuleRest rule = new RuleRest();
		rule.setPortName(port);
		rule.setWidgetName(widget);
		postJSON(SERVER_URL + "rules", rule);
	}


	static void deployEntityType(Class<?> entityType, Class<?> repository) {
		EntityTypeDeployRest rest = new EntityTypeDeployRest();
		rest.setEntity(entityType.getName());
		rest.setRepository(repository.getName());
		postJSON(SERVER_URL + "entities", rest);
	}
	
	static void postEntity(Object entity) {
		postJSON(SERVER_URL + "api/" + entity.getClass().getSimpleName(), entity);
	}

	static void postJSON(String url, Object data) {
	
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
	
			StringEntity input = new StringEntity(new Gson().toJson(data));
			input.setContentType("application/json");
			HttpPost request = new HttpPost(url);
			request.setEntity(input);
			CloseableHttpResponse response = httpClient.execute(request);
	
			Assert.assertEquals(201, response.getStatusLine().getStatusCode());
			
//			String s = response.getEntity().getContent().toString();
	
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	static String post(String url, String data) {
		
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
	
			StringEntity input = new StringEntity(data);
			input.setContentType("application/json");
			HttpPost request = new HttpPost(url);
			request.setEntity(input);
			CloseableHttpResponse response = httpClient.execute(request);
	
			Assert.assertEquals(201, response.getStatusLine().getStatusCode());
			
			return EntityUtils.toString(response.getEntity());
			
	
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
		
		return null;
	}
}
