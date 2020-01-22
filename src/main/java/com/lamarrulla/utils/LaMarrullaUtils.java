package com.lamarrulla.utils;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class LaMarrullaUtils {
	public String recoverParams(HttpServletRequest req) {
		  StringBuilder buffer = new StringBuilder();
		    BufferedReader reader;
		    String data = "";
		    try {
		        reader = req.getReader();
		        String line;
		        while ((line = reader.readLine()) != null) {
		            buffer.append(line);
		        }
		        data = buffer.toString();   
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		    return data;
	  }
}
