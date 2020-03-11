package lamarrulla;

import java.io.IOException;
import java.util.Iterator;

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
	private String campos;
	private String valores;
	//private String respuesta;
	String[] parametros;

	@Override
	  public void doGet(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {
		try {
			response.setContentType("text/json");
		    response.setCharacterEncoding("UTF-8");
			
		    tabla = "";
		    idTabla = "0";
		    //respuesta = "";
			
//			if(request.getPathInfo().length()==0) {
//				respuesta = "Se esperaban los parametros de entrada";
//				response.getWriter().print("{\"respuesta\":\"" + respuesta + "\"}");
//				return;
//			}else {
//				parametros = request.getPathInfo().split("/"); 
//				if(parametros.length>2) {
//					idTabla = parametros[2];
//				}
//				tabla = parametros[1];
//			}
		    
		    if(getParameters(request)==0) {
		    	response.getWriter().print(jso);
				return;
		    }			
						System.out.println(tabla + " " + idTabla);
						utils.setIdTipoPeticion(1);
						utils.setTabla(tabla);
						utils.setIdTabla(Integer.parseInt(idTabla));
						utils.ejecutaConsultaAPI();
						jso = new JSONObject();
						jso = utils.getJso();
						response.getWriter().print(jso);
		}catch(Exception ex) {
			response.getWriter().print("{\"error\":\"" + ex.getMessage() + "\"}");
		}
	  }
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {		
		try {
			response.setContentType("text/json");
			response.setCharacterEncoding("UTF-8");
			tabla = "";
			campos = "";
			valores = "";
			if(getParameters(request)==0) {
				response.getWriter().print(jso);
				return;
			}
			utils = new Utils();
			String parametrosEntrada = utils.recoverParams(request);
			if(parametrosEntrada.length()==0) {
				response.getWriter().print("{\"error\":\"no existen parametros de entrada en el body\"}");
				return;
			}
			jso = new JSONObject(parametrosEntrada);
			generaInsert(jso);
			utils.setIdTipoPeticion(2);
			utils.setTabla(tabla);
			utils.setCampos(campos);
			utils.setValores(valores);
			utils.ejecutaConsultaAPI();
			jso = utils.getJso();
			response.getWriter().print(jso);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print("{\"salida\":\""+ e.getMessage() + "\"}");
		}
	}
	
	private void generaInsert(JSONObject jso) throws JSONException {						
		
		@SuppressWarnings("unchecked")
		Iterator<String> keys = jso.keys();
        String[] CamposA = new String[jso.length()];
        String[] ValoresA = new String[jso.length()];
        String ValorPaso = "";
        int i = 0;
        
        while(keys.hasNext()) {
            String key = keys.next();                
            if (jso.get(key) instanceof Integer) {
            	System.out.println(jso.getInt(key));
            	ValorPaso = String.valueOf(jso.getInt(key));
            }
            else if(jso.get(key) instanceof Boolean) {
            	System.out.println(jso.getBoolean(key));
            	ValorPaso = String.valueOf(jso.getBoolean(key));
            }else {
            	System.out.println(jso.getString(key));
            	ValorPaso = "''" + jso.getString(key) + "''";
            }
            CamposA[i] = key;
            ValoresA[i] = ValorPaso;
            i++;
        }
        campos = String.join(",", CamposA);
        valores = String.join(",", ValoresA);
	}
	
	@Override
	  public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			response.setContentType("text/json");
			response.setCharacterEncoding("UTF-8");
			if(getParameters(request)<=1) {
				response.getWriter().print(jso);
				return;
			}
			utils.setIdTipoPeticion(4);
			utils.setTabla(tabla);
			utils.setIdTabla(Integer.parseInt(idTabla));
			utils.ejecutaConsultaAPI();
			jso = utils.getJso();
			response.getWriter().print(jso);
//			if(idTabla == 0) {
//				System.out.println("no se procesara idtabla = " + idTabla);
//				jso = new JSONObject("{error:\"Se tiene que seleccionar el identificador a borrar\"}");				
//			}else {
//				utils.setIdTipoPeticion(4);
//				utils.setTabla(tabla);
//				utils.setIdTabla(idTabla);
//				utils.ejecutaConsultaAPI();
//				jso = utils.getJso();
//			}		
		}catch(Exception ex) {
			System.out.println(ex.getMessage());			
			response.getWriter().print("{error:\"" + ex.getMessage() + "\"}");
		}
	}
	public int getParameters(HttpServletRequest request) throws JSONException {
		int regresa = 0;
		if(request.getPathInfo() == null) {
			System.out.println("no se proporciono tabla");			
			jso = new JSONObject("{error:\"Se tiene que seleccionar la tabla\"}");
			//response.getWriter().print(jso);
			regresa = 0;
		}						
		else {
			parametros = request.getPathInfo().split("/");
			if(parametros.length==0) {
				System.out.println("no se proporciono tabla");			
				jso = new JSONObject("{error:\"Se tiene que seleccionar la tabla\"}");
				//response.getWriter().print(jso);		
				regresa = 0;
			}else {
				if(parametros.length<=2) {				
					System.out.println("no se procesara idtabla = " + idTabla);			
					jso = new JSONObject("{error:\"Se tiene que seleccionar el identificador\"}");
					//response.getWriter().print(jso);		
					regresa = 1;
				}
				else if(parametros.length>2) {
					idTabla = parametros[2];
					regresa = 2;
				}
				tabla = parametros[1];
			}					
		}
		return regresa;
	}
}