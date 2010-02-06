package com.zycus.dotproject.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
/**
 * 
 * this will help to debug in future. since we are planing to make
 * this project as a jnlp client so not output is expected. thats why 
 * Storing this error in a specific location.
 * 
 * @author Aniruddha Dutta Chowdhury
 * @since : Apr 4, 2009 : 12:05:29 PM
 *
 */
public class ErrorStream extends PrintStream {
	private static String ErrorFileName = ApplicationContext.getProjectFileHome() + "/dpError.log"; 
	public ErrorStream() throws FileNotFoundException {
		super(new FileOutputStream(ErrorFileName, true));
	}
}