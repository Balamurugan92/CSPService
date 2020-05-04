package com.rabo.csp.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.csp.constants.ResponseCodeDescription;
import com.rabo.csp.controller.CSPController;
import com.rabo.csp.model.StatementResponse;
import com.rabo.csp.model.StatmentServiceResponse;
import com.rabo.csp.parser.Transaction;
import com.rabo.csp.service.CSPService;

/**
 * @author 739243
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = CSPController.class, secure = false)
public class CSPControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	CSPService cspService;

	List<Transaction> csvTransactions = new ArrayList<>();
	List<Transaction> xmlTransactions = new ArrayList<>();

	@Before
	public void setUpForCSV() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setReference(Long.valueOf("112806"));
		transaction.setAccountNumber("NL27SNSB0917829871");
		transaction.setDescription("Clothes for Willem Dekker");
		transaction.setStartBalance(91.23);
		transaction.setMutation(15.57);
		transaction.setEndBalance(106.8);
		csvTransactions.add(transaction);
		transaction = new Transaction();
		transaction.setReference(Long.valueOf("112806"));
		transaction.setAccountNumber("NL69ABNA0433647324");
		transaction.setDescription("Clothes for Richard de Vries");
		transaction.setStartBalance(90.83);
		transaction.setMutation(-10.91);
		transaction.setEndBalance(79.92);
		csvTransactions.add(transaction);
		transaction = new Transaction();
		transaction.setReference(Long.valueOf("112806"));
		transaction.setAccountNumber("NL91RABO0315273637");
		transaction.setDescription("Tickets from Richard Bakker");
		transaction.setStartBalance(102.12);
		transaction.setMutation(45.87);
		transaction.setEndBalance(147.99);
		csvTransactions.add(transaction);
	}
	
	@Before
	public void setUpForXML() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setReference(Long.valueOf("167875"));
		transaction.setAccountNumber("NL93ABNA0585619023");
		transaction.setDescription("Tickets from Erik de Vries");
		transaction.setStartBalance(Double.valueOf(5429));
		transaction.setMutation(Double.valueOf(-939));
		transaction.setEndBalance(Double.valueOf(6368));
		xmlTransactions.add(transaction);
		transaction = new Transaction();
		transaction.setReference(Long.valueOf("165102"));
		transaction.setAccountNumber("NL93ABNA0585619023");
		transaction.setDescription("Tickets for Rik Theu");
		transaction.setStartBalance(Double.valueOf(3980));
		transaction.setMutation(Double.valueOf(+1000));
		transaction.setEndBalance(Double.valueOf(4981));
		xmlTransactions.add(transaction);
	}

	@Test
	public void testForCSVFileSuccess() throws Exception {

		StatmentServiceResponse response = new StatmentServiceResponse();
		response.setTransactions(csvTransactions);
		response.setServiceResponse(ResponseCodeDescription.SUCCESS);
		MockMultipartFile multipartFile = new MockMultipartFile("file", "records.csv", "application/vnd.ms-excel",
				"CSP".getBytes());

		when(this.cspService.processCSVStatement(any())).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("mock/CsvSuccessResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(multipart("/api/v1/rabo-csp/process").file(multipartFile))
				.andExpect(status().isOk()).andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}

	@Test
	public void testForXMLFileSuccess() throws Exception {

		StatmentServiceResponse response = new StatmentServiceResponse();
		response.setTransactions(xmlTransactions);
		response.setServiceResponse(ResponseCodeDescription.SUCCESS);
		MockMultipartFile multipartFile = new MockMultipartFile("file", "records.xml", "text/xml",
				"CSP".getBytes());

		when(this.cspService.processXMLStatement(any())).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("mock/XmlSuccessResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(multipart("/api/v1/rabo-csp/process").file(multipartFile))
				.andExpect(status().isOk()).andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}
	
	@Test
	public void testProcessForInValidFile() throws Exception {

		MockMultipartFile multipartFile = new MockMultipartFile("file", "records.test", "multipart/form-data",
				"CSP".getBytes());

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("mock/InvalidFileResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(multipart("/api/v1/rabo-csp/process").file(multipartFile))
				.andExpect(status().is4xxClientError()).andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}
}
