package com.howudodat.db;

import java.util.ArrayList;

import org.dominokit.jackson.annotation.JSONMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JSONMapper
@JsonIgnoreProperties(ignoreUnknown=true)
public class DBResults {
	public boolean success;
	public ArrayList<DBHome>homes;
	
	public DBResults() { }

	public String toString() {
		return DBResults_MapperImpl.INSTANCE.write(this);
	}
}
