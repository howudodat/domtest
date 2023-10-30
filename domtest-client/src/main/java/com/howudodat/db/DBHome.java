package com.howudodat.db;

import java.util.ArrayList;

import org.dominokit.jackson.ObjectMapper;
import org.dominokit.jackson.annotation.JSONMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JSONMapper
@JsonIgnoreProperties(ignoreUnknown=true)
public class DBHome extends DBObject {
	public String FacilityName;

	@Override
	public final String toPickerString() {
		return FacilityName;
	}


	@Override
	public String toString() {
		return DBHome_MapperImpl.INSTANCE.write(this);
	}

	@JSONMapper
	public interface ArrayListMapper extends ObjectMapper<ArrayList<DBHome>> {}

}
