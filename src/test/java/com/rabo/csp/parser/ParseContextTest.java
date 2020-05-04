package com.rabo.csp.parser;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import com.rabo.csp.exception.CSPException;
import com.rabo.csp.service.StatementParser;

/**
 * @author 739243
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ParseContextTest {

	@InjectMocks
	private ParseContext parserTest;

	@Mock
	private StatementParser parser;

	List<Transaction> transactions = new ArrayList<>();

	@Before
	public void setUpForCSV() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setReference(Long.valueOf("112806"));
		transaction.setAccountNumber("NL27SNSB0917829871");
		transaction.setDescription("Clothes for Willem Dekker");
		transaction.setStartBalance(91.23);
		transaction.setMutation(15.57);
		transaction.setEndBalance(106.8);
		transactions.add(transaction);
		transaction = new Transaction();
		transaction.setReference(Long.valueOf("112806"));
		transaction.setAccountNumber("NL69ABNA0433647324");
		transaction.setDescription("Clothes for Richard de Vries");
		transaction.setStartBalance(90.83);
		transaction.setMutation(-10.91);
		transaction.setEndBalance(79.92);
		transactions.add(transaction);
		transaction = new Transaction();
		transaction.setReference(Long.valueOf("112806"));
		transaction.setAccountNumber("NL91RABO0315273637");
		transaction.setDescription("Tickets from Richard Bakker");
		transaction.setStartBalance(102.12);
		transaction.setMutation(45.87);
		transaction.setEndBalance(147.99);
		transactions.add(transaction);
	}

	@Test
	public void testForSuccess() throws Exception {

		when(this.parser.parse(any())).thenReturn(transactions);
		List<Transaction> actual = parserTest.parseFile(new ClassPathResource("records.csv").getFile());
		assertEquals(transactions, actual);
	}

	@Test(expected = CSPException.class)
	public void testForException() throws Exception {

		when(this.parser.parse(any())).thenThrow(CSPException.class);

		List<Transaction> actual = parserTest.parseFile(new ClassPathResource("records.csv").getFile());
		assertEquals(transactions, actual);
	}

}
