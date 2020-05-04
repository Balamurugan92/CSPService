package com.rabo.csp.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rabo.csp.constants.ResponseCode;
import com.rabo.csp.model.Response;
import com.rabo.csp.model.ServiceResponse;
import com.rabo.csp.model.StatementProcessStatus;
import com.rabo.csp.model.Status;

/**
 * @author 739243
 *
 */
public interface ResponseUtil {

	public static ResponseEntity<?> getEntityForException(ResponseCode responseCode, Response response) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	public static ResponseEntity<?> getEntityForBadRequest(ResponseCode responseCode, Response response) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	public static ResponseEntity<?> getEntityForOk(ServiceResponse serviceResponse, Response response) {
		Status status = response.getStatus();
		if (serviceResponse != null) {
			status.setStatusCode(serviceResponse.getServiceResponse().getCode());
			status.setStatusDescription(serviceResponse.getServiceResponse().getDescrption());
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	public static StatementProcessStatus handleFileFormatError(String statusCode, String statusDesc) {

		StatementProcessStatus status = new StatementProcessStatus();
		status.setStatusCode(statusCode);
		status.setStatusDescription(statusDesc);
		return status;
	}
}
