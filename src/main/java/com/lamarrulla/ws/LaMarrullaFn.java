package com.lamarrulla.ws;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		name="LaMarrullaFnAppEngine",
		urlPatterns= {"/lamarrullafn/*"}
		)

public class LaMarrullaFn extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print("{\"error\":\"la marrulla sp\"}");
	}
}
