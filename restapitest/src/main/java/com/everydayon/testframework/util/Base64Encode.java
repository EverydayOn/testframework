package com.everydayon.testframework.util;

import org.apache.commons.codec.binary.Base64;

/**
 * A simple base64 encoded utility 
 * 
 * @author Jagadesh Babu Munta
 *
 */
public class Base64Encode {
	
	public static void main(String []args) {
		
		byte [] pass = Base64.encodeBase64(args[0].getBytes());
		byte [] dpass = Base64.decodeBase64(args[0].getBytes());
		System.out.println("Encoded string: "+ new String(pass));
		//System.out.println("Decoded string: "+ new String(dpass));
	}

}
