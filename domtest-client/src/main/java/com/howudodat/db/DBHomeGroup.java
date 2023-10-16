package com.howudodat.db;

import java.util.ArrayList;

import org.dominokit.jackson.ObjectMapper;
import org.dominokit.jackson.annotation.JSONMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JSONMapper
@JsonIgnoreProperties(ignoreUnknown=true)
public class DBHomeGroup {
	public String hgroup = "";
	public long QBID;
	
	@Override
	public String toString() {
		return DBHomeGroup_MapperImpl.INSTANCE.write(this);
	}

	@JSONMapper
	public interface ArrayListMapper extends ObjectMapper<ArrayList<DBHomeGroup>> {}

}
