package com.lamarrulla.interfaces;

import org.json.JSONObject;

public interface IAPI {
	public JSONObject jsonObject = new JSONObject();
	public String consulta = "";
	public void ejecutaAPI();
}
