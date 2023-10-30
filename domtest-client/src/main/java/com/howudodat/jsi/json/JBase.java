package com.howudodat.jsi.json;

import elemental2.core.Global;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class JBase {
	public long id;

	public final @JsOverlay String toJson() {
		return Global.JSON.stringify(this, (key, value) -> {
			return (key.startsWith("_") ? Js.undefined() : value);
		});
	}

	// this is handled in each class, just provide a default here
	// public @JsOverlay String toPickerString() {
	// 	return ""+id;
	// }

	// public final @JsOverlay String toString() {
	// 	return toJson();
	// }

}
