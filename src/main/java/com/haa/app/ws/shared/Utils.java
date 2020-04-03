package com.haa.app.ws.shared;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {
	
	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private final int USERID_LENGTH = 30;
	private final int ADDRESSID_LENGTH = 30;
	
	public int getUserIdLength()
	{
		return USERID_LENGTH;
	}
	
	public String generateUserId(int length)
	{
		return generateRandomString(length);
	}
	
	public int getAddressIdLength()
	{
		return ADDRESSID_LENGTH;
	}
	
	public String generateAddressId(int length)
	{
		return generateRandomString(length);
	}
	
	/*
	 * Generate a random String of characters of specified length
	 */
	public String generateRandomString(int length)
	{
		StringBuilder returnValue = new StringBuilder(length);
		
		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		
		return new String(returnValue);
	}

}
