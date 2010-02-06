package com.zycus.dotproject.bo;

public enum UserType {
	DefaultUser(0), 
	Administrator(1), 
	CEO(2),
	Director(3),
	BranchManager(4),
	Manager(5), 
	Supervisor(6), 
	Employee(7);
	
	private int value = 0;
	
	public int getValue() {
		return value;
	}
	
	UserType(int value)
	{
		this.value = value;
	}

	public static UserType valueOf(int id) 
	{
		switch ((int) id) {
		case 0:
			return DefaultUser;
		case 1:
			return Administrator;
		case 2:
			return CEO;
		case 3:
			return Director;
		case 4:
			return BranchManager;
		case 5:
			return Manager;
		case 6:
			return Supervisor;
		case 7:
			return Employee;
		default:
			return DefaultUser;
		}
	}
}