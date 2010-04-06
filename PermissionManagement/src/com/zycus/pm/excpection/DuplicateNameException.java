package com.zycus.pm.excpection;

public class DuplicateNameException extends Exception {

	private static final long	serialVersionUID	= 1L;
	
	public static enum DuplicateNameType{
		DuplicateGroupName, DuplicateRightName
	}
	
	private DuplicateNameType duplicateNameType = null;
	
	public DuplicateNameException(DuplicateNameType duplicateNameType) {
		this.duplicateNameType = duplicateNameType;
	}


	public DuplicateNameException(String message, Throwable cause, DuplicateNameType duplicateNameType) {
		super(message, cause);
		this.duplicateNameType = duplicateNameType;
	}

	public DuplicateNameException(String message, DuplicateNameType duplicateNameType) {
		super(message);
		this.duplicateNameType = duplicateNameType;
	}



	public DuplicateNameException(Throwable cause, DuplicateNameType duplicateNameType) {
		super(cause);
		this.duplicateNameType = duplicateNameType;
	}



	public DuplicateNameType getDuplicateNameType() {
		return duplicateNameType;
	}
	
	public void setDuplicateNameType(DuplicateNameType duplicateNameType) {
		this.duplicateNameType = duplicateNameType;
	}

}