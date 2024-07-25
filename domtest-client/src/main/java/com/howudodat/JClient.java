package com.howudodat;

import elemental2.core.Global;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

public class JClient {
	@JsType(isNative = true, namespace = JsPackage.GLOBAL, name="Object")
	public static class JsonClient {
		public String LastName;
		public String First1;
		public String First2;
		public long Age;
		public int Weight;
	}

	public String LastName;
	public String First1;
	public String First2;
	public long Age;
	public int Weight;

	public int changed = 0;

	public JClient() {

	}

	public JClient(JsonClient obj) {
		_parseMap(obj);
	}
	public JClient(String json) {
		JsonClient js = (JsonClient)Global.JSON.parse(json);
		_parseMap(js);
	}

	protected void _parseMap(JsonClient obj) {
		this.LastName = obj.LastName;
		this.First1 = obj.First1;
		this.First2 = obj.First2;
		this.Age = obj.Age;
		this.Weight = obj.Weight;
	}

}
