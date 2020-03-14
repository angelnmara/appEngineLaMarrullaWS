package com.lamarrulla.ws;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.lamarrulla.database.DbAcces;
import com.lamarrulla.utils.Utils;
import com.lamarrulla.security.ProtectUserPassword;

@WebServlet(
		name="AltaUsuarioAppEngine",
		urlPatterns= {"/altausuario"}
		)

public class AltaUsuario extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	Utils utils = new Utils();
	//JsonObject jso = new JsonObject();
	JSONObject jso = new JSONObject();
	DbAcces dbacces;
	
	String username;
	String correo;
	String password;
	
	int idUsuario;
	int idPassw;
	
	//String salida;
	String strError;
	
	boolean isUsuarioInsertado;
	boolean isPasswInsertado = false;
	

	@Override
	  public void doGet(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {

	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");

	    response.getWriter().print("Hello Alta usuario App Engine!\r\n");

	  }
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws IOException{
		
		response.setContentType("text/json");
	    response.setCharacterEncoding("UTF-8");
		String parametrosEntrada = utils.recoverParams(request);
		//jso = new JsonParser().parse(parametrosEntrada).getAsJsonObject();
		try {
			jso = new JSONObject(parametrosEntrada);
			
			username = jso.has("username")?jso.getString("username"):null;
			correo = jso.has("correo")?jso.getString("correo"):null;
			password = jso.has("password")?jso.getString("password"):null;
			
			System.out.println(username);
			System.out.println(correo);
			System.out.println(password);
			
			insertaUsuario(username, correo);
					
			
		    
			if(!isUsuarioInsertado) {								
			    response.getWriter().print(strError);
			}else {
				ProtectUserPassword protectedUser = new ProtectUserPassword();
				protectedUser.setMyPassword(password);
				protectedUser.generaPassword();
				insertaPassword(username, password);
				if(!isPasswInsertado) {
					response.getWriter().print(strError);
				}else {
					response.getWriter().print("{\"respuesta\":\"Usuario se inserto correctamente\"}");
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			response.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
			e.printStackTrace();			
		}
		
//		username = jso.get("username").getAsString();
//		correo = jso.get("correo").getAsString();
//		password = jso.get("password").getAsString();
		
		

		
	}
	
	private void insertaUsuario(String username, String correo) {
		// TODO Auto-generated method stub
		//boolean respuesta = false;
		try {
			String consulta = "insert into tbUsu(fcUsuNom, fcUsuCorrElec) \r\n" + 
							"values ('" + username + "', '" + correo + "') returning fiIdUsu;";
			if(username==null||correo==null) {
				consulta = consulta.replace("'null'", "null");
			}
			System.out.println(consulta);
			dbacces = new DbAcces();
			dbacces.connectDatabase();
			if(dbacces.isValid()) {
				dbacces.setStrQuery(consulta);
				dbacces.execQry();
			}			
			//dbacces.disconnectDatabase();
			
			//objAPI.setConsulta(consulta);
			//objAPI.ejecutaAPI();
			//String respuestaConsulta = objAPI.getstJS();
			
			//String respuestaConsulta = String.valueOf(dbacces.getIdReturned());
			
			if(dbacces.getStrError() == null) {
				idUsuario = Integer.parseInt(dbacces.getStrResult());
				isUsuarioInsertado = true;				
			}else {
				//salida = String.format(error, respuestaConsulta.replaceAll("\"", "'"));
				System.out.println("Existe un error al insertar tbUsu " + dbacces.getStrError());
				isUsuarioInsertado = false;
				strError = dbacces.getStrError();
			}
		}catch(Exception ex) {
			strError = ex.getMessage();
			//dbacces.disconnectDatabase();
		}finally {
			dbacces.disconnectDatabase();
		}
	} 
	
	private void insertaPassword(String username, String password) {
		// TODO Auto-generated method stub
		//boolean respuesta = false;
		try {
			ProtectUserPassword pup = new ProtectUserPassword();
			pup.setMyPassword(password);
			pup.generaPassword();
			String consulta = "insert into tbUsuPassw(fiIdUsu, fcUsuPassw, fcSecPassw, fcSalt)\r\n" + 
					"values(" + idUsuario + ", crypt('" + password + "', gen_salt('bf')), '" + pup.getMySecurePassword() + "', '" + pup.getSalt() + "') returning fiIdUsuPassw;";
			dbacces = new DbAcces();
			dbacces.connectDatabase();
			if(dbacces.isValid()) {
				dbacces.setStrQuery(consulta);
				dbacces.execQry();
			}						
			
//			objAPI.setConsulta(consulta);
//			objAPI.ejecutaAPI();			
			//String respuestaConsulta = objAPI.getstJS();
			//respuestaConsulta.matches("-?\\d+")
			
			if(dbacces.getStrError() == null) {
				idPassw = Integer.parseInt(dbacces.getStrResult());
				isPasswInsertado = true;				
			}else {
				//salida = String.format(error, respuestaConsulta.replaceAll("\"", "'"));
				System.out.println("Ocurrio un error al insertar en tbUsuPAssw: " + dbacces.getStrError());
				strError = dbacces.getStrError();
			}					
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			strError = ex.getMessage();
		}finally {
			dbacces.disconnectDatabase();
		}
	}
}
