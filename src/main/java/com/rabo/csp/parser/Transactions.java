package com.rabo.csp.parser;

import java.util.List;

/**
 * @author 739243
 *
 */
public interface Transactions {

	public List<Transaction> getStatements();

	public void setStatements(List<Transaction> transactions);
}
