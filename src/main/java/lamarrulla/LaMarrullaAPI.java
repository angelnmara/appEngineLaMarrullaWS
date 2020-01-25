package lamarrulla;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.lamarrulla.implement.Secured;
import com.lamarrulla.utils.Utils;

@WebServlet(
		name="LaMarrullaAPIAppEngine",
		urlPatterns= {"/lamarrullaapi"}
		)

public class LaMarrullaAPI extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Utils utils = new Utils();
	
	JSONObject jso = new JSONObject();
	private String tabla;
	private String idTabla;
	private String respuesta;

	@Override
	@Secured
	  public void doGet(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {
		response.setContentType("text/json");
	    response.setCharacterEncoding("UTF-8");
		String parametrosEntrada = utils.recoverParams(request);
		
		if(parametrosEntrada.length()==0) {
			respuesta = "Se esperaban los parametros de entrada";
			response.getWriter().print("{\"respuesta\":\"" + respuesta + "\"}");
			return;
		}
		
		try {
			jso = new JSONObject(parametrosEntrada);
			tabla = jso.has("tabla")?jso.getString("tabla"):"";
			idTabla = jso.has("idTabla")?jso.getString("idTabla"):"";
			System.out.println(tabla + " " + idTabla);
			utils.setIdTipoPeticion(1);
			utils.setTabla(tabla);
			utils.setIdTabla(Integer.parseInt(idTabla));
			utils.ejecutaConsultaAPI();
			jso = utils.getJso();
			response.getWriter().print(jso);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print("{\"respuesta\":\"" + e.getMessage() + "\"}");
		}	    
	  }
}
