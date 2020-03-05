package lamarrulla;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

//import com.lamarrulla.implement.Secured;
import com.lamarrulla.utils.Utils;

@WebServlet(
		name="LaMarrullaAPIAppEngine",
		urlPatterns= {"/lamarrullaapi/*"}
		)

public class LaMarrullaAPI extends HttpServlet {	
	private static final long serialVersionUID = 1L;
	Utils utils = new Utils();
	
	JSONObject jso = new JSONObject();
	private String tabla;
	private String idTabla;
	private String respuesta;
	String[] parametros;

	@Override
	  public void doGet(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {
		response.setContentType("text/json");
	    response.setCharacterEncoding("UTF-8");
		
	    tabla = "";
	    idTabla = "0";
	    respuesta = "";
		
		if(request.getPathInfo().length()==0) {
			respuesta = "Se esperaban los parametros de entrada";
			response.getWriter().print("{\"respuesta\":\"" + respuesta + "\"}");
			return;
		}else {
			parametros = request.getPathInfo().split("/"); 
			if(parametros.length>2) {
				idTabla = parametros[2];
			}
			tabla = parametros[1];
		}
		
					System.out.println(tabla + " " + idTabla);
					utils.setIdTipoPeticion(1);
					utils.setTabla(tabla);
					utils.setIdTabla(Integer.parseInt(idTabla));
					utils.ejecutaConsultaAPI();
					jso = new JSONObject();
					jso = utils.getJso();
					response.getWriter().print(jso);	    
	  }
}