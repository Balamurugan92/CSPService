package com.rabo.csp.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rabo.csp.constants.CSPConstants;
import com.rabo.csp.exception.CSPException;
import com.rabo.csp.service.StatementParser;

/**
 * @author 739243
 *
 */
@Component(value = "csvFileParser")
public class CSVFileParser implements StatementParser {

	private static final String[] FILE_HEADER_MAPPING = { CSPConstants.REF_NUMB, CSPConstants.ACCOUNT_NUM,
			CSPConstants.DESC, CSPConstants.START_BAL, CSPConstants.MUTATION, CSPConstants.END_BAL };

	private static final Logger LOG = LoggerFactory.getLogger(CSVFileParser.class);

	/**
	 * CSV File parsing : Extracting records from the file
	 */
	@SuppressWarnings("resource")
	@Override
	public List<Transaction> parse(File file) throws CSPException {

		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
		List<Transaction> transactions;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
			LOG.info("CSV file parsing..");
			CSVParser csvParser = new CSVParser(fileReader, csvFileFormat);
			List<CSVRecord> csvRecords = csvParser.getRecords();
			LOG.info("Collecting statements from csv file");
			transactions = csvRecords.stream().skip(1)
					.map(record -> new Transaction(Long.parseLong(record.get(CSPConstants.REF_NUMB)),
							record.get(CSPConstants.ACCOUNT_NUM), record.get(CSPConstants.DESC),
							Double.parseDouble(record.get(CSPConstants.START_BAL)),
							Double.parseDouble(record.get(CSPConstants.MUTATION)),
							Double.parseDouble(record.get(CSPConstants.END_BAL))))
					.collect(Collectors.toList());
			LOG.info("Collected statements from csv file");
		} catch (Exception exception) {
			LOG.error("Error in parsing CSV File", exception);
			throw new CSPException(exception);
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();
			} catch (IOException exception) {
				LOG.error("Error in parsing CSV File", exception);
			}
		}
		return transactions;
	}
}
