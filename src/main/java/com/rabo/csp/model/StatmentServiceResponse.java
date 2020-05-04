package com.rabo.csp.model;

import java.util.List;

import com.rabo.csp.constants.ResponseCode;
import com.rabo.csp.parser.Transaction;

/**
 * @author 739243
 *
 */
public class StatmentServiceResponse implements ServiceResponse {

	private ResponseCode serviceResponse;
	private List<Transaction> transactions;

	@Override
	public ResponseCode getServiceResponse() {
		return serviceResponse;
	}

	public void setServiceResponse(ResponseCode serviceResponse) {
		this.serviceResponse = serviceResponse;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

}
