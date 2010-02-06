package com.zycus.dotproject.exception;

public class GenericException extends RuntimeException {
	private static final long	serialVersionUID	= 1l;

	public GenericException() {
		super();
	}

	public GenericException(String message, Throwable cause) {
		super(message, cause);
	}

	public GenericException(String message) {
		super(message);
	}

	public GenericException(Throwable cause) {
		super(cause);
	}

}