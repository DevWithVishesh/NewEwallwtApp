package com.icsd.service;

import org.springframework.web.multipart.MultipartFile;

import com.icsd.model.Customer;
import com.icsd.model.CustomerDocuments;

import java.io.IOException;

public interface CustDocServ {

	 int savedoc(Customer cstmer,String filetype,MultipartFile file) throws IOException;
	 CustomerDocuments fromCustDocId(int custdocid);
	
}
