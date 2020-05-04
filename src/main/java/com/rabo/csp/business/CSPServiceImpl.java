package com.rabo.csp.business;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rabo.csp.constants.ResponseCodeDescription;
import com.rabo.csp.exception.CSPException;
import com.rabo.csp.model.StatmentServiceResponse;
import com.rabo.csp.parser.CSVFileParser;
import com.rabo.csp.parser.ParseContext;
import com.rabo.csp.parser.Transaction;
import com.rabo.csp.parser.XMLFileParser;
import com.rabo.csp.service.CSPService;

/**
 * @author 739243
 *
 */
@Service
public class CSPServiceImpl implements CSPService {

	private static final Logger LOG = LoggerFactory.getLogger(CSPServiceImpl.class);

	@Autowired
	ParseContext parseContext;

	@Qualifier("csvFileParser")
	@Autowired
	CSVFileParser csvFileParser;

	@Qualifier("xmlFileParser")
	@Autowired
	XMLFileParser xmlFileParser;

	/**
	 * Processing CSV File
	 * 1. Extract the given CSV file
	 * 2. Filter the duplicate records
	 * 3. Validate the mutation
	 */
	@Override
	public StatmentServiceResponse processCSVStatement(File file) {
		List<Transaction> transactions;
		StatmentServiceResponse statmentServiceResponse = new StatmentServiceResponse();
		try {
			LOG.info("CSV File process started..");
			parseContext.setParser(csvFileParser);
			transactions = collectFailureRecords(parseContext.parseFile(file));
			statmentServiceResponse.setTransactions(transactions);
			statmentServiceResponse.setServiceResponse(ResponseCodeDescription.SUCCESS);
			if (transactions.isEmpty())
				statmentServiceResponse.setServiceResponse(ResponseCodeDescription.NO_DATE_FOUND);
			LOG.info("CSV File process ended..");
		} catch (CSPException exception) {
			LOG.error("Error in CSV File process..", exception);
			statmentServiceResponse.setServiceResponse(ResponseCodeDescription.ERROR);
		}
		return statmentServiceResponse;
	}

	/**
	 * Processing XML File
	 * 1. Extract the given CSV file
	 * 2. Filter the duplicate records
	 * 3. Validate the mutation
	 */
	@Override
	public StatmentServiceResponse processXMLStatement(File file) {
		List<Transaction> transactions;
		StatmentServiceResponse statmentServiceResponse = new StatmentServiceResponse();
		try {
			LOG.info("XML File process started..");
			parseContext.setParser(xmlFileParser);
			transactions = collectFailureRecords(parseContext.parseFile(file));
			statmentServiceResponse.setTransactions(transactions);
			statmentServiceResponse.setServiceResponse(ResponseCodeDescription.SUCCESS);
			if (transactions.isEmpty())
				statmentServiceResponse.setServiceResponse(ResponseCodeDescription.NO_DATE_FOUND);
			LOG.info("XML File process ended..");
		} catch (CSPException exception) {
			LOG.error("Error in XML File process..", exception);
			statmentServiceResponse.setServiceResponse(ResponseCodeDescription.ERROR);
		}
		return statmentServiceResponse;
	}

	/**
	 * @param transactions
	 * @return add duplicate and non valid records to form failed transactions
	 */
	private List<Transaction> collectFailureRecords(List<Transaction> transactions) {
		List<Transaction> failureRecords = validateMutation(transactions);
		failureRecords.addAll(filterDuplicateReference(transactions));
		return failureRecords;
	}

	/**
	 * @param transactions
	 * @return filter duplicate records by reference
	 * Take only those elements which exists more than once
	 */
	private List<Transaction> filterDuplicateReference(List<Transaction> transactions) {
		List<Transaction> duplicate = transactions.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
				.filter(e -> e.getValue() > 1L).map(Entry<Transaction, Long>::getKey).collect(Collectors.toList());

		return transactions.stream().map(stmt -> {
			for (Transaction dupStmt : duplicate) {
				if (dupStmt.getReference().equals(stmt.getReference())) {
					return stmt;
				}
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	/**
	 * @param transactions
	 * @return Validate based on mutation
	 */
	private List<Transaction> validateMutation(List<Transaction> transactions) {
		return transactions.stream().filter(transaction -> !isValid(transaction)).collect(Collectors.toList());
	}

	private boolean isValid(Transaction transaction) {
		return Math.round(transaction.getEndBalance() - transaction.getStartBalance()) == Math
				.round(transaction.getMutation());
	}
}
