package com.everydayon.testframework.restapitest;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import sun.util.logging.resources.logging;
import static org.testng.Assert.*;

import com.jayway.restassured.RestAssured;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * A simple test using RestAssured test framework
 * 
 * @author Jagadesh Babu Munta
 *
 */
public class RestAssuredTest {
	Logger logger = Logger.getLogger(RestAssuredTest.class);
	@BeforeClass
	public void setup() {
		RestAssured.baseURI = System.getProperty("api.host","https://api.github.com");
		//RestAssured.port = Integer.parseInt(System.getProperty("api.host.port",""));
		RestAssured.basePath = "/";
	}
	
	@Test(dataProvider="testData")
	public void basicLoginTest(String userName, String password) {	
		//System.out.println(userName);
		given().
			log().everything().
			headers("Accept", "application/vnd.github.v3+json").
			auth().
			preemptive().
			basic(userName, password).
		expect().statusCode(200).log().all().
		get("/");
		
		RestAssured.authentication = preemptive().basic(userName, password);
		
		given().
		log().all().
		headers("Accept", "application/vnd.github.v3+json").
		expect().statusCode(200).log().all().
		get("/authorizations");		
		
		given().
			log().everything().
			headers("Accept", "application/vnd.github.v3+json").
			expect().statusCode(200).log().all().
			get("/users/jagadeshbmunta/orgs");
		
	}
	
	@AfterClass
	public void unsetup() {
		RestAssured.reset();
		
	}
	
	@DataProvider
	public Object[][] testData() {
		return new Object[][] {
				{"jagadeshbmunta","test123!"},
				{"test","test123"}
		};
	}
	
}
