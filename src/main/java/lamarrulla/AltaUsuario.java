package lamarrulla;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;
import com.lamarrulla.database.DbAcces;
import com.lamarrulla.utils.LaMarrullaUtils;
import com.lamarrulla.security.ProtectUserPassword;

@WebServlet(
		name="AltaUsuarioAppEngine",
		urlPatterns= {"/altausuario"}
		)

public class AltaUsuario extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	LaMarrullaUtils utils = new LaMarrullaUtils();
	JsonObject jso = new JsonObject();
	DbAcces dbacces;
	
	String username;
	String correo;
	String password;
	
	int idUsuario;
	int idPassw;
	
	//String salida;
	String strError;
	
	boolean isUsuarioInsertado = false;
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
		
		String parametrosEntrada = utils.recoverParams(request);
		jso = new JsonParser().parse(parametrosEntrada).getAsJsonObject();
		
		username = jso.get("username").getAsString();
		correo = jso.get("correo").getAsString();
		password = jso.get("password").getAsString();

		System.out.println(username);
		System.out.println(correo);
		System.out.println(password);
		
		insertaUsuario(username, correo);
				
		response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");
	    
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
				response.getWriter().print("Usuario se inserto correctamente");
			}
		}
	}
	
	private void insertaUsuario(String username, String correo) {
		// TODO Auto-generated method stub
		//boolean respuesta = false;
		try {
			String consulta = "insert into tbUsu(fcUsuNom, fcUsuCorrElec) \r\n" + 
							"values ('" + username + "', '" + correo + "') returning fiIdUsu;";
			System.out.println(consulta);
			dbacces = new DbAcces();
			dbacces.connectDatabase();
			dbacces.setStrQuery(consulta);
			dbacces.execQry();
			dbacces.disconnectDatabase();
			
			//objAPI.setConsulta(consulta);
			//objAPI.ejecutaAPI();
			//String respuestaConsulta = objAPI.getstJS();
			
			//String respuestaConsulta = String.valueOf(dbacces.getIdReturned());
			
			if(dbacces.getStrError() == null) {
				idUsuario = dbacces.getIdReturned();
				isUsuarioInsertado = true;				
			}else {
				//salida = String.format(error, respuestaConsulta.replaceAll("\"", "'"));
				System.out.println("Existe un error al insertar tbUsu " + dbacces.getStrError());	
				strError = dbacces.getStrError();
			}
		}catch(Exception ex) {
			strError = ex.getMessage();
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
			dbacces.setStrQuery(consulta);
			dbacces.execQry();
			dbacces.disconnectDatabase();
			
//			objAPI.setConsulta(consulta);
//			objAPI.ejecutaAPI();			
			//String respuestaConsulta = objAPI.getstJS();
			//respuestaConsulta.matches("-?\\d+")
			
			if(dbacces.getStrError() == null) {
				idPassw = dbacces.getIdReturned();
				isPasswInsertado = true;				
			}else {
				//salida = String.format(error, respuestaConsulta.replaceAll("\"", "'"));
				System.out.println("Ocurrio un error al insertar en tbUsuPAssw: " + dbacces.getStrError());
				strError = dbacces.getStrError();
			}					
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			strError = ex.getMessage();
		}		
	}
}
