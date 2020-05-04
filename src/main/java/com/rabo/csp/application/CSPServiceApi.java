package com.rabo.csp.application;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.rabo.csp.model.StatementResponse;
import com.rabo.csp.swagger.Documentation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author 739243
 *
 */
@RequestMapping("/api/v1/rabo-csp/")
@Api(tags = { "Customer Statement Service" })
public interface CSPServiceApi {

	@PostMapping(value = "process", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(notes = Documentation.STATEMENT_PROCESS_FAILED, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "CS File Processor", response = StatementResponse.class)
	public ResponseEntity<StatementResponse> processCustomerStatement(MultipartFile inputFile);
}
