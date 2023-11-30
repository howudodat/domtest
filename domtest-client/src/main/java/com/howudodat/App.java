package com.howudodat;

import org.dominokit.domino.ui.forms.DateBox;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.style.DominoCss;
import org.dominokit.domino.ui.utils.DominoUIConfig;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.gwtproject.i18n.client.DateTimeFormat;

import com.google.gwt.core.client.EntryPoint;

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
		String items[] = { "Alice", "Allen", "Bob", "Brian"};

		DominoUIConfig.CONFIG.setClosePopupOnBlur(true);
		DateBox dp = DateBox.create("Updated").addCss(dui_w_1_2p);
		MiniPicker<String> mp1 = MiniPicker.create(items);
		MiniPicker<String> mp2 = MiniPicker.create(items);

		layout.withContent((parent1, content) -> {
			content.appendChild(dp);
			content.appendChild(mp1);
			content.appendChild(mp2);

			mp1.setSelectedObject("Allen");
		});

		body().appendChild(layout);
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
