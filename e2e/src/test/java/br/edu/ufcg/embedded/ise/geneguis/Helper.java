package br.edu.ufcg.embedded.ise.geneguis;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint;
import br.edu.ufcg.embedded.ise.geneguis.backend.WidgetType;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.EntityTypeDeployRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.JsonMetadata;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.PortRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.RuleRest;
import br.edu.ufcg.embedded.ise.geneguis.backend.controller.WidgetRest;
import br.edu.ufcg.embedded.ise.geneguis.jpadomain.MetadataUtil;

public class Helper {

	static final String SERVER_URL = "http://localhost:8080/";

	static void openApp() {
		WebBrowserTestCase.driver.get(SERVER_URL);
	}

	static void clickEntityType(Class<?> entityType) {
		click("entityType_" + entityType.getSimpleName());
	}

	static void click(String id) {
		checkId(id).click();
	}

	static void checkTitle(Class<?> entityType) {
		checkId("title_" + entityType.getSimpleName());
	}

	static WebElement checkId(String id) {
		return WebBrowserTestCase.driver.findElement(By.id(id));
	}

	static void checkIds(String... ids) {
		for (String id : ids) {
			WebBrowserTestCase.driver.findElement(By.id(id));
		}
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
		URL resource = EntryPoint.class.getResource("/widgets/" + fileName + ".hbs");
		File file = new File(resource.getFile());
		String filePath = file.getAbsolutePath();
		try {
			Path widgetPath = Paths.get(filePath);
			return new String(Files.readAllBytes(widgetPath));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
			return null;
		}
	}

	static void rule(String port, String widget, WidgetType type) {
		RuleRest rule = new RuleRest(widget, port, type.name());
		postJSON(SERVER_URL + "rules", rule);
	}

	static <T> void deployEntityType(Class<T> entityType, Class<?> repository) throws Exception {
		EntityTypeDeployRest rest = new EntityTypeDeployRest();
		rest.setEntity(entityType.getName());
		rest.setRepository(repository.getName());
		postJSON(SERVER_URL + "entities", rest);
	}

	static <T> T postEntity(T entity) {
		return postEntity(SERVER_URL + "api/" + entity.getClass().getSimpleName(), entity);
	}

	@SuppressWarnings("unchecked")
	static <T> T postJSON(String url, T data) {

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			ObjectMapper mapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			mapper.setDateFormat(df);

			StringEntity input = new StringEntity(mapper.writeValueAsString(data));
			input.setContentType("application/json");
			HttpPost request = new HttpPost(url);
			request.setEntity(input);
			CloseableHttpResponse response = httpClient.execute(request);

			Assert.assertEquals(201, response.getStatusLine().getStatusCode());

			String json = EntityUtils.toString(response.getEntity(), "UTF-8");

			return (T) mapper.readValue(json, data.getClass());

		} catch (IOException e) {
			Assert.fail(e.getMessage());
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	static <T> T postEntity(String url, T data) {
		
		EntityType entityType = MetadataUtil.extractMetadata(data.getClass());

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			StringEntity input = new StringEntity(JsonMetadata.renderInstance(data, entityType).toString());
			input.setContentType("application/json");
			HttpPost request = new HttpPost(url);
			request.setEntity(input);
			CloseableHttpResponse response = httpClient.execute(request);

			Assert.assertEquals(201, response.getStatusLine().getStatusCode());

			String json = EntityUtils.toString(response.getEntity(), "UTF-8");

			Object newInstance = data.getClass().newInstance();
			JsonMetadata.parseInstance(entityType, json, newInstance);
			return (T) newInstance;

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
			return null;
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
