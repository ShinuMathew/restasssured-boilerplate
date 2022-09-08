package com.apitest.bot.tests;

import java.util.HashMap;

import javax.annotation.Nullable;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.apitest.bot.enums.HttpStatusCode;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import net.minidev.json.JSONObject;

public class BaseTest {
	
	RequestSpecBuilder builder;
	RequestSpecification requestSpec;
	RequestSpecification request;
	@Nullable
	int otp;
	// Deep scan in rest assured jayway 
	
	@BeforeTest
	public void testSetUp() {
		this.builder = new RequestSpecBuilder();
		builder.setBaseUri("https://otpbroadcaster-webhook.herokuapp.com");		
	}
	
	@BeforeMethod
	public void setRequest() {		
		this.requestSpec = this.builder.build();	
		this.request = RestAssured.given(this.requestSpec);
	}

	@Test(enabled = true, priority = 1)
	public void getAllOTPs() {		
		this.request = this.request.basePath("/otp-broadcaster/otp-all");
		this.request = this.request.cookies(new HashMap<>());
		this.requestSpec = this.request;
		Response res = this.requestSpec.get();
		System.out.println(res.body().asString());
		Assert.assertEquals(res.statusCode(), HttpStatusCode.SUCCESS.getStatusCodeValue());
	}
	
	@Test(dependsOnMethods = {"getAllOTPs"}, enabled = true, priority = 2)
	public void broadCastOTP() {
		this.otp = (int) Math.ceil(Math.random() * 100000);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("otp", otp);
		System.out.println(otp);
		
		this.request = this.request.basePath("otp-broadcaster/send-otp");
		this.request = this.request.cookies(new HashMap<String, Object>());
		this.request = this.request.body(jsonObject.toJSONString());
		this.requestSpec = request;
		Response res = this.requestSpec.post();
		System.out.println(res.body().asString());
		Assert.assertEquals(res.statusCode(), HttpStatusCode.SUCCESS.getStatusCodeValue());
	}
	
	@Test(dependsOnMethods = {"broadCastOTP"}, enabled = true, priority = 3)
	public void fetchLatestOTP() throws InterruptedException {
		Thread.sleep(3000); // Think time
		this.request = this.request.basePath("/otp-broadcaster/otp-latest");
		this.requestSpec = this.request;
		Response res = this.requestSpec.get();
		System.out.println(res.body().asString());
		Assert.assertEquals(res.statusCode(), HttpStatusCode.SUCCESS.getStatusCodeValue());
		String otpString = JsonPath.from(res.body().asString()).get("$.otp");
		Assert.assertEquals(Integer.parseInt(otpString), this.otp);
	}
	
	
	@AfterMethod
	public void tearDown() {
//		this.spec = null;		
	}
	
}
