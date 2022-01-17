package person.it.web;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;

import io.restassured.RestAssured;

@ExtendWith(DBUnitExtension.class)
public class PersonWebIT {

	private Map<String, String> buildRequestPayload(String firstName, String lastName) {
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("firstName", firstName);
		payload.put("lastName", lastName);
		return payload;
	}

	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.basePath = "/person-web/app/person";
		RestAssured.port = 8080;
	}

	@Test
	public void postCreated() {
		Map<String, String> payload = buildRequestPayload("Nikola", "Tesla");
		String responseBody = with().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.body(payload).when().post().then().statusCode(201).header(HttpHeaders.LOCATION, endsWith("/1"))
				.extract().asString();
		Approvals.verify(responseBody);
	}

	@Test
	@DataSet(value = "yml/nikolaTesla.yml")
	public void putOk() {
		Map<String, String> payload = buildRequestPayload("Nikola-UPDATED", "Tesla-UPDATED");
		String responseBody = with().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.body(payload).when().put("/{id}", 1).then()
				.statusCode(200).extract().asString();
		Approvals.verify(responseBody);
	}

	@Test
	@DataSet(value = "yml/albertEinstein.yml")
	public void getByIdOk() {
		String responseBody = when().get("/{id}", 2).then().statusCode(200).extract().asString();
		Approvals.verify(responseBody);
	}

	@Test
	@DataSet(value = "yml/empty.yml")
	public void getByIdNotFound() {
		when().get("/{id}", 999).then().statusCode(404).body(is(emptyOrNullString()));
	}

	@Test
	@DataSet(value = "yml/carlSagan.yml")
	public void deleteNoContent() {
		when().delete("/{id}", 3).then().statusCode(204).body(is(emptyOrNullString()));
	}

	@Test
	@DataSet(value = "yml/everybody.yml")
	public void getAllOk() {
		String responseBody = when().get().then().statusCode(200).extract().asString();
		Approvals.verify(responseBody);
	}

}
