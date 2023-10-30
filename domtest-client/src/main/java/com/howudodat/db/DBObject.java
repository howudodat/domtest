package com.howudodat.db;

public abstract class DBObject {
	public int id = -1;
	public int id() { return id; }
	
	public abstract String toPickerString(); 
	public abstract String toString();
	
}
