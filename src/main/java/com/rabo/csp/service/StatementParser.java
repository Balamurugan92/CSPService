package com.rabo.csp.service;

import java.io.File;
import java.util.List;

import com.rabo.csp.exception.CSPException;
import com.rabo.csp.parser.Transaction;

/**
 * @author 739243
 *
 */
public interface StatementParser {

	public List<Transaction> parse(File file) throws CSPException;

}
