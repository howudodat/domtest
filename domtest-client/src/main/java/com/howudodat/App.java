package com.howudodat;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.style.DominoCss;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.gwtproject.i18n.client.DateTimeFormat;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import elemental2.core.JsArray;
import elemental2.promise.Promise;
import jsinterop.base.Js;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint, ElementsFactory, DominoCss {
	protected AppLayout layout = AppLayout.create("Domino-ui test");
	public static DateTimeFormat sdfLongMs = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss.SSS");

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		layout.withContent((parent1, content) -> {
			content.appendChild(Button.create("write entries").addClickListener(l->writeEntries()));
			content.appendChild(Button.create("read single").addClickListener(l->readOne()));
			content.appendChild(Button.create("read all").addClickListener(l->readEntries()));
		});

		body().appendChild(layout);
	}

	/**
	 * write the test entries, NB: Apparently we dont need to wait for the promises, they 
	 * seem to run asynchronously without stepping on each other, but I'm doubtful
	 */
	protected void writeEntries() {
		for (int x=0; x<25; x++) {
			String key = "Key"+x;
			String value = "value:";  for (int y=0;y<30;y++) value += "0123456789";
			Idb.SetData(key, value);
		}
	}

	protected void readOne() {
		Idb.GetData("Key1").then(result -> {
			GWT.log(result.a + " = " + result.b);
			return null;
		});
	}

	protected void readEntries() {
		JsArray<Promise<Pair<String,String>>>jaPromises = new JsArray<>();
		for (int x=0; x<25; x++) {
			jaPromises.push(
				Idb.GetData("Key"+x)
			);
		}
		Promise.all(Js.cast(jaPromises)).then(results -> {
			// YAY This one works
			JsArray<Pair<String,String>> arr = Js.cast(results);
			for (Pair<String, String>p : arr.asList())
				GWT.log(p.a + " = " + p.b);

			// This one doesn't
			for (Pair<String, String>p : (Pair<String,String>[])results)
				GWT.log(p.a + " = " + p.b);

			return null;
		});
	}

	public static native void time(String timer)
	/*-{
		console.time(timer);
	}-*/;

	public static native void timeEnd(String timer)
	/*-{
		console.timeEnd(timer);
	}-*/;

	public static native void timeLog(String timer, String log)
	/*-{
		console.timeLog(timer, log);
	}-*/;
}
