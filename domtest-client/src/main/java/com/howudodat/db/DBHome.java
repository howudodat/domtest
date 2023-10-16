package com.howudodat.db;

import java.util.ArrayList;
import java.util.HashMap;

import org.dominokit.jackson.ObjectMapper;
import org.dominokit.jackson.annotation.JSONMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JSONMapper
@JsonIgnoreProperties(ignoreUnknown=true)
public class DBHome {
	public String FacilityName;
	public String slug;
	public boolean Full;
	public boolean Stop;
	public boolean Review;
	public double lat;
	public double lon;
	public String Website;
	public String LicenseNumber;
	public long Beds;
	public long Open_BC;
	public long placed;
	public long referred;
	public boolean OffWeb;
	public long hgroup;
	public long Contract;
	public boolean Deleted;
	public long Neighborhood;
	public String L_Map;  // neighborhood text string
	public long Appearance;

	public boolean SSI;
	public boolean VA;
	public boolean ALW;
	public boolean HO;
	
	public ArrayList<DBHomeAddress>addresses = new ArrayList<>();
	public ArrayList<DBHomeAttributes>mapAttributes = new ArrayList<>();
	public DBHomeGroup dbHomeGroup;
	public ArrayList<DBHome>dbHomeGroups = new ArrayList<>();

	@JsonIgnore protected HashMap<Long, DBHomeAttributes>mapAttrs = null;
	@JsonIgnore public boolean isFullData = false;	// flag to indicate if we have all the home data or not
	
	@Override
	public String toString() {
		return DBHome_MapperImpl.INSTANCE.write(this);
	}

	@JSONMapper
	public interface ArrayListMapper extends ObjectMapper<ArrayList<DBHome>> {}

}
