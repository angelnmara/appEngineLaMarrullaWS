package com.lamarrulla.implement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lamarrulla.interfaces.IAPI;

public class ImplementAPI implements IAPI {
	private String consulta;
	
	private String stJS="";
	/*
		Tipo Regresa 
		0.- String
		1.- JSON
	*/
	private int tipoRegresa = 0;

	public int getTipoRegresa() {
		return tipoRegresa;
	}

	public void setTipoRegresa(int tipoRegresa) {
		this.tipoRegresa = tipoRegresa;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}
	
	public String getstJS() {
		return stJS;
	}
	
	public void setstJS(String stJS) {
		this.stJS = stJS;
	}

	public void ejecutaAPI() {
		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");
		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;

		}

		System.out.println("PostgreSQL JDBC Driver Registered!");

		Connection connection = null;

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/lamarrulladb", "postgres",
					"maradr");

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");			
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			try {				
				System.out.println("You made it, take control your database now!");			
				Statement st = connection.createStatement();				
				ResultSet rs = st.executeQuery(consulta);															
				while(rs.next()) {
					System.out.print("columna uno regresada ");
					stJS+=rs.getString(1);				
				}
				System.out.println(stJS);						
				rs.close();
				st.close();
			}catch(Exception ex) {
				System.out.println("ocurrio un error: " +ex.getMessage());
				stJS = ex.getMessage();
			}			
		} else {
			System.out.println("Failed to make connection!");			
		}
	}
}
