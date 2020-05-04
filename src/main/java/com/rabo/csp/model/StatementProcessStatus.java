package com.rabo.csp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

/**
 * @author 739243
 *
 */
@ApiModel(value = "Statement Process Status")
public class StatementProcessStatus implements Status {

	@JsonProperty(value = "status_code")
	private String statusCode;

	@JsonProperty(value = "status_desc")
	private String statusDescription;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

}
