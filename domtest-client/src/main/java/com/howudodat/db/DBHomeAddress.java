package com.howudodat.db;

import java.util.ArrayList;

import org.dominokit.jackson.ObjectMapper;
import org.dominokit.jackson.annotation.JSONMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JSONMapper
@JsonIgnoreProperties(ignoreUnknown=true)
public class DBHomeAddress {
	public String Street = "";
	public String City = "";
	public String State = "";
	public String Zip = "";
	public long Type;
	
	@Override
	public String toString() {
		return DBHomeAddress_MapperImpl.INSTANCE.write(this);
	}

	@JSONMapper
	public interface ArrayListMapper extends ObjectMapper<ArrayList<DBHomeAddress>> {}

}
