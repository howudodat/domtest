package com.howudodat.jsi.json;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name="Object")
public class JHome extends JBase {
	public String FacilityName;
	public int referred;
	public int placed;
	public String _myLocalString;
	public String Last_Update;
	public long Last_Update_By;
	public long hroup;
	public long Contract;
	public boolean Deleted;

 }
