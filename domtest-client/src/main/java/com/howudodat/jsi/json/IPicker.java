package com.howudodat.jsi.json;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name="Object")
public interface IPicker {
	@JsOverlay public abstract String toPickerString() ;
}