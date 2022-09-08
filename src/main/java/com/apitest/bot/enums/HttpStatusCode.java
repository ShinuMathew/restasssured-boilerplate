package com.apitest.bot.enums;

public enum HttpStatusCode {
	
	SUCCESS(200),
	CREATED(201),
	REDIRECTED(301),
	BAD_REQUEST(400),
	UNAUTHORISED(401),
	FORBIDDEN(403),
	NOT_FOUND(404),
	METHOD_NOT_ALLOWED(405),
	CONFLICT(409),
	TOO_MANY_REQUESTS(429),
	SERVER_ERROR(500),
	GATEWAY_TIMEOUT(502);
	
	private int statusCode;
	
	HttpStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public int getStatusCodeValue() {
		return this.statusCode;
	}

}
