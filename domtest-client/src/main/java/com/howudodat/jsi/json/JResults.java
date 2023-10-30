package com.howudodat.jsi.json;

import elemental2.core.Global;
import elemental2.core.JsArray;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name="Object")
public class JResults extends JBase {
	public boolean success;
	public JsArray<JHome> homes;

	@JsOverlay public static JResults Parse(String sjson) {
		return (JResults) Global.JSON.parse(sjson);
	}
}