package com.zycus.pm.utility;

public final class NullValidator {
	public static void validateNullEntries(Object ...arguments) {
		for(Object argument : arguments) {
			if(argument == null) {
				throw new IllegalArgumentException("Null parameter passed");
			}
		}
	}
}