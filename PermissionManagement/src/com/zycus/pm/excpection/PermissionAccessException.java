package com.zycus.pm.excpection;

/**
 * this class will be thrown in case of generic error / exception happens in
 * permission management component. one simple additional, exception generation
 * time is just kept
 * 
 * @author Aniruddha Dutta Chowdhury
 * @since : Apr 13, 2009 : 1:03:03 PM
 * 
 */
public class PermissionAccessException extends Exception {

	private static final long	serialVersionUID	= 1L;
	private long				exceptionTime		= System.currentTimeMillis();

	public PermissionAccessException() {
		exceptionTime = System.currentTimeMillis();
	}

	public PermissionAccessException(String message, Throwable cause) {
		super(message, cause);
		exceptionTime = System.currentTimeMillis();
	}

	public PermissionAccessException(String message) {
		super(message);
		exceptionTime = System.currentTimeMillis();
	}

	public PermissionAccessException(Throwable cause) {
		super(cause);
		exceptionTime = System.currentTimeMillis();
	}

	public long getExceptionTime() {
		return exceptionTime;
	}

	public void setExceptionTime(long exceptionTime) {
		this.exceptionTime = exceptionTime;
	}
}