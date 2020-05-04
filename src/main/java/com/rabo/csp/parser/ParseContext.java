package com.rabo.csp.parser;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rabo.csp.exception.CSPException;
import com.rabo.csp.service.StatementParser;


/**
 * @author 739243
 *
 */
@Component
public class ParseContext {

	private StatementParser parser;

	public void setParser(StatementParser parser) {
		this.parser = parser;
	}

	public List<Transaction> parseFile(File file) throws CSPException {
		return parser.parse(file);
	}

}