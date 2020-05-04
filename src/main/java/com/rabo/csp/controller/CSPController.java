package com.rabo.csp.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rabo.csp.application.CSPServiceApi;
import com.rabo.csp.constants.CSPConstants;
import com.rabo.csp.constants.ResponseCodeDescription;
import com.rabo.csp.model.StatementResponse;
import com.rabo.csp.model.StatmentServiceResponse;
import com.rabo.csp.model.Transaction;
import com.rabo.csp.service.CSPService;
import com.rabo.csp.util.ResponseUtil;

/**
 * @author 739243
 *
 */
@RestController
@RequestMapping("api/v1/rabo-csp")
public class CSPController implements CSPServiceApi {

	public static final Logger LOG = LoggerFactory.getLogger(CSPController.class);

	@Autowired
	CSPService statementService;

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<StatementResponse> processCustomerStatement(
			@RequestParam(CSPConstants.FILE) MultipartFile file) {

		StatementResponse response = new StatementResponse();
		StatmentServiceResponse serviceResponse;
		try {
			LOG.info("Starting service : /process : PUT");
			if (CSPConstants.CSV_FILE_TYPE.equals(file.getContentType())) {
				String fileName = file.getOriginalFilename();
				LOG.info(CSPConstants.PROCESSING_CSV_FILE, fileName);
				// Transfer the multipart request data to new file
				File csvFile = new File(fileName);
				file.transferTo(csvFile);
				// Invoke CSV file process call
				serviceResponse = statementService.processCSVStatement(csvFile);
			} else if (CSPConstants.XML_FILE_TYPE.equals(file.getContentType())) {
				String fileName = file.getOriginalFilename();
				LOG.info(CSPConstants.PROCESSING_XML_FILE, fileName);
				// Transfer the multipart request data to new file
				File csvFile = new File(fileName);
				file.transferTo(csvFile);
				// Invoke XML file process call
				serviceResponse = statementService.processXMLStatement(csvFile);
			} else {
				LOG.error(CSPConstants.INVALID_FORMAT);
				response.setStatus(
						ResponseUtil.handleFileFormatError(CSPConstants.ERRCODE, CSPConstants.INVALID_FORMAT));
				return (ResponseEntity<StatementResponse>) ResponseUtil
						.getEntityForBadRequest(ResponseCodeDescription.VALIDATION_ERROR, response);
			}
			// Take ref and desc from failed transactions
			List<Transaction> transactions = new ArrayList<>();
			if (serviceResponse != null && serviceResponse.getTransactions() != null) {
				serviceResponse.getTransactions().forEach(transaction -> {
					Transaction records = new Transaction();
					records.setReference(transaction.getReference());
					records.setDescription(transaction.getDescription());
					transactions.add(records);
				});
			}
			response.setTransaction(transactions);
		} catch (Exception exception) {
			LOG.info("Error in serving : /process : PUT");
			LOG.error(CSPConstants.EXCEPTION_IN_PROCESSING_FILE, exception);
			return (ResponseEntity<StatementResponse>) ResponseUtil
					.getEntityForException(ResponseCodeDescription.INTERNAL_SERVER_ERROR, response);
		}
		return (ResponseEntity<StatementResponse>) ResponseUtil.getEntityForOk(serviceResponse, response);
	}
}
