package com.howudodat.db;

import java.util.ArrayList;

import org.dominokit.jackson.ObjectMapper;
import org.dominokit.jackson.annotation.JSONMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JSONMapper
@JsonIgnoreProperties(ignoreUnknown=true)
public class DBHomeAttributes {
	public long Home;
	public long Attribute;
	public Integer Attribute_Int = 0;
	public double Attribute_Min;
	public double Attribute_Max;
	public boolean Checked;
	
	@Override
	public String toString() {
		return DBHomeAttributes_MapperImpl.INSTANCE.write(this);
	}

	@JSONMapper
	public interface ArrayListMapper extends ObjectMapper<ArrayList<DBHomeAttributes>> {}

}
