package com.lamarrulla.ws;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.lamarrulla.database.DbAcces;
import com.lamarrulla.utils.Utils;
import com.lamarrulla.security.Token;
import com.lamarrulla.security.VerifyProvidedPassword;

@WebServlet(
		name="AutenticationAppEngine",
		urlPatterns= {"/autenticacion"}
		)

public class Authentication extends HttpServlet {
		
	Utils utils;
	Token token = new Token();
	
	//JsonObject jso = new JsonObject();
	JSONObject jso = new JSONObject();
	DbAcces dbacces;
	VerifyProvidedPassword vpp = new VerifyProvidedPassword();
	
	private static final long serialVersionUID = 1L;
	String username;
	String correo;
	String password;
	String salida;
	
	
	@Override
	  public void doGet(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {

	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");

	    response.getWriter().print("Autentication get!\r\n");
	  }
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws IOException{
		try {
			utils = new Utils();
			String parametrosEntrada = utils.recoverParams(request);
			response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
		//jso = new JsonParser().parse(parametrosEntrada).getAsJsonObject();		
				
			jso = new JSONObject(parametrosEntrada);	
			
			username = jso.has("username")?jso.getString("username"):"";
			correo = jso.has("correo")?jso.getString("correo"):"";
			password = jso.has("password")?jso.getString("password"):"";  //.get("password").toString();
			
			System.out.println(username);
			System.out.println(correo);
			System.out.println(password);
			
			String token = "";
			
			// Authenticate the user using the credentials provided
			authenticate(username, correo, password);
			
			
			String mostrar;
			if(username.length()==0) {
				mostrar = correo;
			}else {
				mostrar = username;
			}
			
			if(vpp.verificaPassword()) {
				// Issue a token for the user
				token = issueToken(mostrar);
			}else {
				System.out.println("no validado");
			}

			salida = utils.getStringFromXML("responseToken");					   
		    response.getWriter().print(String.format(salida, token, jso.getString("fcsalt"), mostrar));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
		}								
	}
	
	private void authenticate(String username, String correo, String password) throws JSONException {
		// TODO Auto-generated method stub		
		recuperaPassword(username, correo, password);		
		vpp.setSecurePassword(jso.getString("fcsecpassw"));
		vpp.setSalt(jso.getString("fcsalt"));
		vpp.setProvidedPassword(password);		
	}
	
	private void recuperaPassword(String username, String correo,  String password) {
		// TODO Auto-generated method stub
		String consulta = "select fcsecpassw, fcsalt, fcusunom\n" + 
				"from tbusupassw a\n" + 
				"inner join tbusu b\n" + 
				"on a.fiidusu = b.fiidusu\n" + 
				"where fcusupassw = crypt('" + password + "', fcusupassw)\n";
		
		if((username!=null&&!username.isEmpty()) && (correo!=null&&!correo.isEmpty())) {
			consulta += "and (fcusunom = '" + username + "' and fcusucorrelec = '" + correo + "')";
		}else if(username!=null&&!username.isEmpty()) {
			consulta += "and fcusunom = '" + username + "'";
		}else if(correo!=null&&!correo.isEmpty()){
			consulta += "and fcusucorrelec = '" + correo + "'";
		}
				
		utils = new Utils();
		utils.setConsulta(consulta);
		utils.ejecutaConsultaJSON();			
		jso = utils.getJso();
		System.out.println(jso.toString());
	}
	private String issueToken(String username) throws JSONException {
		// TODO Auto-generated method stub		
		token.setUser(username);
		token.setSecret(jso.getString("fcsalt"));
		token.CreateToken();
		String Token = token.getToken();
		return Token;
	}
}
