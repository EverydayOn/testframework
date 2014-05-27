package com.everydayon.testframework.restapitest;

import org.apache.commons.codec.binary.Base64;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import sun.util.logging.resources.logging;
import static org.testng.Assert.*;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * A simple github test using RestAssured test framework
 * 
 * @author Jagadesh Babu Munta
 *
 */
public class GitHubRATest {
	Logger logger = Logger.getLogger(GitHubRATest.class);
	@BeforeClass
	public void setup() {
		RestAssured.baseURI = System.getProperty("api.host","https://api.github.com");
		//RestAssured.port = Integer.parseInt(System.getProperty("api.host.port",""));
		RestAssured.basePath = "/";
	}
	
	@Test(dataProvider="testData")
	public void basicLoginTest1(String userName, String password, int expStatusCode, String expData) {
		
		//System.out.println(userName);
		given().
			log().everything().
			headers("Accept", "application/vnd.github.v3+json").
			auth().
			preemptive().
			basic(userName, new String(Base64.decodeBase64(password))).
		expect().statusCode(expStatusCode).log().all().
		get("/");		
		
	}

	@Test(dataProvider="testData")
	public void basicLoginTest2(String userName, String password, int expStatusCode,String expData) {	
		RestAssured.authentication = preemptive().basic(userName, new String(Base64.decodeBase64(password)));
		
		given().
			log().all().
			headers("Accept", "application/vnd.github.v3+json").
		expect().statusCode(expStatusCode).log().all().
		get("/authorizations");		
		
		ResponseBody resBody = given().
			log().everything().
			headers("Accept", "application/vnd.github.v3+json").
		expect().statusCode(expStatusCode).log().all().
		get("/users/jagadeshbmunta/orgs").body();
		
		//assertEquals(resBody.path("url"),expData);
		Response res = given().
				log().everything().
				headers("Accept", "application/vnd.github.v3+json").
			expect().statusCode(expStatusCode).log().all().
			get("/users/jagadeshbmunta/orgs");
		
		String json = res.asString();
		System.out.println(">>>>>>>>>>>>>Json="+json);
		JsonPath j = new JsonPath(json);
		System.out.println(">>>>>>>>>>>>>[0].url="+j.get("[0].url"));
		assertEquals(j.get("[0].url"),expData);
	}
	
	@AfterClass
	public void unsetup() {
		RestAssured.reset();
		
	}
	
	@DataProvider
	public Object[][] testData() {
		return new Object[][] {
				{"jagadeshbmunta","dGVzdDEyMyE=", 200,"https://api.github.com/orgs/EverydayOn"},
				{"test","dGVzdDEyMy=",401,null}
		};
	}
	
}
