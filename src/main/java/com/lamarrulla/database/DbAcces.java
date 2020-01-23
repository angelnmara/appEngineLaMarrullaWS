package com.lamarrulla.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbAcces {
	static String server = System.getenv("server");
	static String puerto = System.getenv("puerto");
	static String database = System.getenv("database");
	static String user = System.getenv("user");
	static String passw = System.getenv("passw");
	static int tipoDatabase = Integer.parseInt(System.getenv("tipoDatabase"));	// 1 Local // 2 SQLEngine
	private String instance = System.getenv("instance");
	Connection connection = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	boolean closeRs;
	boolean closePs;
	boolean closeConnection;
	
	private String strQuery;
	//int idReturned;
	private String strResult;
	private String strError;
	
		
	public String getStrResult() {
		return strResult;
	}
//	public void setStrResult(String strResult) {
//		this.strResult = strResult;
//	}
	public String getStrError() {
		return strError;
	}
//	public int getIdReturned() {
//		return idReturned;
//	}
//	public void setIdReturned(int idReturned) {
//		this.idReturned = idReturned;
//	}
	public String getStrQuery() {
		return strQuery;
	}
	public void setStrQuery(String strQuery) {
		this.strQuery = strQuery;
	}
	
	public ResultSet getRs() {
		return rs;
	}
	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
	
	public void connectDatabase() {
        try {
            // We register the PostgreSQL driver
            // Registramos el driver de PostgresSQL
            try { 
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }            
            
            // Database connect
            
            switch(tipoDatabase) {
            	case 1:
            		// Conectamos con la base de datos
            		connection = DriverManager.getConnection(
                            "jdbc:postgresql://" + server + ":" + puerto + "/" + database,
                            user, passw);
            		break;
            	case 2:
            		// conectamos con gcloud            
            		String jdbcUrl = String.format(
            			    "jdbc:postgresql://google/%s?socketFactory=com.google.cloud.sql.postgres.SocketFactory"
            			        + "&cloudSqlInstance=%s",
            			    database,
            			    instance);
            		connection = DriverManager.getConnection(jdbcUrl, user, passw);
//            		String j = "jdbc:postgresql://google/" + database + "?cloudSqlInstance=" + instance + "&socketFactory=com.google.cloud.sql.postgres.SocketFactory&user=" + user + "&password=" + passw;
//            		connection = DriverManager.getConnection(jdbcUrl);

                    break;
                default:
                	System.out.println("No se ha definido una conexion a base de datos");
                	break;                	            	
            }                                                         

            boolean valid = connection.isValid(50000);
            System.out.println(valid ? "TEST OK" : "TEST FAIL");
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error: " + sqle);
        }
    }
	public void disconnectDatabase() {				
		try {
			if(rs!=null) {
				rs.close();
				closeRs = rs.isClosed();				
			}
			if(ps!=null) {
				ps.close();
				closePs = ps.isClosed();
			}
			if(connection!=null) {
				connection.close();
				closeConnection = connection.isClosed();
			}
							
			//System.out.println(closeConnection||closeRs||closePs?"Coneccion cerrada":"valida la conexion sigue abierta");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Existio un error al cerrar la conexion");
		}		
	}	
	public void execQry() {
		try {
			ps = connection.prepareStatement(strQuery);
			ps.execute();
			rs = ps.getResultSet();
			if(rs!=null) {
				if(rs.next()) {
					strResult = rs.getString(1);
				}	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error al ejecutar insert: " + e.getMessage());
			strError = e.getMessage();
		}					
	}
}
