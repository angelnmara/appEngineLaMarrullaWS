package com.lamarrulla.implement;

import java.io.IOException;
import java.util.Enumeration;

//import javax.annotation.Priority;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.Priorities;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.Provider;
import com.lamarrulla.security.Token;
//import com.lamarrulla.implement.Secured;


//@Secured
//@Provider
//@Priority(Priorities.AUTHENTICATION)
@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {
	
	private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private static final String Usuario = "username";
    private static final String Secret = "secret";
    
    Token token = new Token();
    String tok;
    String user;
    String secret;
//    ContainerRequestContext requestCont;
    ServletRequest requestCont;

/*	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Get the Authorization header from the request
		requestCont = requestContext;
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        // Extract the token from the Authorization header
        tok = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
        
        user = requestContext.getHeaderString(Usuario);
        
        secret = requestContext.getHeaderString(Secret);

        try {

            // Validate the token
            validateToken();

        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }	
	}*/
	
	private boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                    .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }
	
	private void abortWithUnauthorized(ServletRequest requestContext) {

        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
//        requestContext.abortWith(
//                Response.status(Response.Status.UNAUTHORIZED)
//                        .header(HttpHeaders.WWW_AUTHENTICATE, 
//                                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
//                        .build());
		System.out.println("Abortar autorizacion");
    }
	
	private void validateToken() throws Exception {
        // Check if the token was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
		try {
			token.setSecret(secret);
			token.setToken(tok);
			token.setUser(user);
			token.VerifyToken();
			if(!token.isAutenticado()) {
//				abortWithUnauthorized(requestCont);
				return;
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}		
    }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("prueba init filter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// Get the Authorization header from the request
		HttpServletRequest httpRequest = (HttpServletRequest) request;
	    Enumeration<String> headerNames = httpRequest.getHeaderNames();
	    String headerVal;
	    String Token = "";
	    if (headerNames != null) {	    	
	            while (headerNames.hasMoreElements()) {
	            	headerVal = httpRequest.getHeader(headerNames.nextElement());
	            	if(headerVal.contains("Bearer ")) {
	            		Token = headerVal.replace("Bearer ", "");
	            		System.out.println(Token);
	            	}	                    
	            }
	    }
	    if (!isTokenBasedAuthentication(Token)) {
            abortWithUnauthorized(request);
            return;
        }
	    chain.doFilter(httpRequest, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
