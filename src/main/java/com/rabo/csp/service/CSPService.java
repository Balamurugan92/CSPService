package com.rabo.csp.service;

import java.io.File;

import com.rabo.csp.model.StatmentServiceResponse;

/**
 * @author 739243
 *
 */
public interface CSPService {

	public StatmentServiceResponse processCSVStatement(File file);

	public StatmentServiceResponse processXMLStatement(File file);

}
