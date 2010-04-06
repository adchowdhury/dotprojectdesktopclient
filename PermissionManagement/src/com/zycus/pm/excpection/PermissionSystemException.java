package com.zycus.pm.excpection;

/**
 * This exception will be thrown in case there is some unexpected problem like
 * out-of-memory thing happens. one simple additional, exception generation time
 * is just kept
 * 
 * @author Aniruddha Dutta Chowdhury
 * @since : Apr 13, 2009 : 1:07:29 PM
 * 
 */
public class PermissionSystemException extends RuntimeException {

	private static final long	serialVersionUID	= 1L;
	private long				exceptionTime		= System.currentTimeMillis();

	public PermissionSystemException() {
		exceptionTime = System.currentTimeMillis();
	}

	public PermissionSystemException(String message, Throwable cause) {
		super(message, cause);
		exceptionTime = System.currentTimeMillis();
	}

	public PermissionSystemException(String message) {
		super(message);
		exceptionTime = System.currentTimeMillis();
	}

	public PermissionSystemException(Throwable cause) {
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