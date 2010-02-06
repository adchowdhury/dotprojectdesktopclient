package com.zycus.dotproject.exception;

public class InsufficientPrivilagesException extends GenericException
{
	private static final long	serialVersionUID	= 1l;
	private String message = "You do not have sufficient privilages to perform this action";

	public InsufficientPrivilagesException() {
		super("You do not have sufficient privilages to perform this action");
	}

}
