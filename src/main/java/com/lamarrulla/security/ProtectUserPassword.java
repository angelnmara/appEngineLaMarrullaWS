package com.lamarrulla.security;

import com.lamarrulla.security.PasswordUtils;

public class ProtectUserPassword {
	String myPassword;
	public String getMyPassword() {
		return myPassword;
	}

	public void setMyPassword(String myPassword) {
		this.myPassword = myPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getMySecurePassword() {
		return mySecurePassword;
	}

	public void setMySecurePassword(String mySecurePassword) {
		this.mySecurePassword = mySecurePassword;
	}

	String salt;
	String mySecurePassword;
	
	public void generaPassword()
    {        
        // Generate Salt. The generated value can be stored in DB. 
        salt = PasswordUtils.getSalt(30);
        
        // Protect user's password. The generated value can be stored in DB.
        mySecurePassword = PasswordUtils.generateSecurePassword(myPassword, salt);
        
        // Print out protected password 
        System.out.println("My secure password = " + mySecurePassword);
        System.out.println("Salt value = " + salt);
    }
}
