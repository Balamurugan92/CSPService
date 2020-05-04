package com.rabo.csp.constants;

/**
 * @author 739243
 *
 */
public enum ResponseCodeDescription implements ResponseCode {

	INTERNAL_SERVER_ERROR("500", "Process error. Please contact service providor"),
	VALIDATION_ERROR("400", "validation error"), SUCCESS("1001", "file process success"),
	NO_DATE_FOUND("1002", "no failure record found"), ERROR("1003", "error processing file");

	private final String code;

	private final String descrption;

	ResponseCodeDescription(String code, String descrption) {
		this.code = code;
		this.descrption = descrption;
	}

	public String getCode() {
		return code;
	}

	public String getDescrption() {
		return descrption;
	}

}
