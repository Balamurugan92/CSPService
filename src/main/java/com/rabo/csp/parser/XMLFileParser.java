package com.rabo.csp.parser;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rabo.csp.exception.CSPException;
import com.rabo.csp.service.StatementParser;

/**
 * @author 739243
 *
 */
@Component(value = "xmlFileParser")
public class XMLFileParser implements StatementParser {

	private static final Logger LOG = LoggerFactory.getLogger(XMLFileParser.class);

	/**
	 * XML File parsing : Extracting records from the file
	 */
	@Override
	public List<Transaction> parse(File file) throws CSPException {
		Transactions transactions;
		JAXBContext jaxbContext;
		try {
			LOG.info("XML File Parsing started..");
			jaxbContext = JAXBContext.newInstance(XMLTransactions.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			transactions = (XMLTransactions) unmarshaller.unmarshal(file);
			LOG.info("XML File Parsing ended..");
		} catch (Exception exception) {
			LOG.error("Error in XML File Parsing", exception);
			throw new CSPException(exception);
		}
		return transactions.getStatements();
	}
}
