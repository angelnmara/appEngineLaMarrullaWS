package com.lamarrulla.security;

import com.lamarrulla.security.PasswordUtils;

public class VerifyProvidedPassword {
	public String getProvidedPassword() {
		return providedPassword;
	}

	public void setProvidedPassword(String providedPassword) {
		this.providedPassword = providedPassword;
	}

	public String getSecurePassword() {
		return securePassword;
	}

	public void setSecurePassword(String securePassword) {
		this.securePassword = securePassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	// User provided password to validate
	private String providedPassword;
	// Encrypted and Base64 encoded password read from database
	private String securePassword;
	// Salt value stored in database
	private String salt;
	
	public boolean verificaPassword()
    {        
        boolean passwordMatch = PasswordUtils.verifyUserPassword(providedPassword, securePassword, salt);
        
        if(passwordMatch) 
        {
            System.out.println("Provided user password " + providedPassword + " is correct.");
        } else {
            System.out.println("Provided password is incorrect");
        }
        
        return passwordMatch;       
    }
}
