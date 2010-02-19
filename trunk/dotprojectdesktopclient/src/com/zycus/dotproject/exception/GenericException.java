package com.zycus.dotproject.exception;

public class GenericException extends RuntimeException {
	private static final long	serialVersionUID	= 1l;
	private Type				type				= Type.Normal;

	public static enum Type {
		Database, Normal
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public GenericException(String message, Throwable cause) {
		super(message, cause);
	}

	public GenericException(String message, Throwable cause, Type a_type) {
		super(message, cause);
		type = a_type;
	}

	public GenericException(String message) {
		super(message);
	}

	public GenericException(String message, Type a_type) {
		super(message);
		type = a_type;
	}
}