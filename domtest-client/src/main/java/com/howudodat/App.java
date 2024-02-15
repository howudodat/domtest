package com.howudodat;

import org.dominokit.domino.ui.forms.LongBox;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.forms.suggest.Select;
import org.dominokit.domino.ui.forms.suggest.SelectOption;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.style.DominoCss;
import org.dominokit.domino.ui.utils.DominoUIConfig;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.gwtproject.core.client.GWT;
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
		TextBox tb1 = TextBox.create();
		LongBox lb1 = LongBox.create();

		tb1.getInputElement().addEventListener("input", evt -> GWT.log("TextBox Changed"));
		lb1.getInputElement().addEventListener("input", evt -> GWT.log("LongBox Changed"));

		layout.withContent((parent1, content) -> {
			content.appendChild(tb1);
			content.appendChild(lb1);
		});

		body().appendChild(layout);
	}

	public void onModuleLoadXX() {
		String items[] = { "", "Alice Long Last Name", "Allen With Last Name", "Bob Forgot My Last Name", "Brian No Last Name"};

		DominoUIConfig.CONFIG.setClosePopupOnBlur(true);
		Select<String> sel = Select.<String>create().addCss(dui_clearable);
		Select<String> sel2 = Select.<String>create().addCss(dui_clearable);
		sel.setWidth("150px");
		sel2.setWidth("150px");

		for (String s : items) sel.appendChild(SelectOption.<String>create(s, s, s));
		for (String s : items) sel2.appendChild(SelectOption.<String>create(s, s, s));
		sel.setSearchable(true);
		sel2.setSearchable(true);
		sel.getOptionsMenu().setFitToTargetWidth(false);
		sel.getOptionsMenu().setDropDirection(DropDirection.BOTTOM_RIGHT);
		sel2.getOptionsMenu().setDropDirection(DropDirection.BOTTOM_RIGHT);

		layout.withContent((parent1, content) -> {
			content.appendChild(sel);
			content.appendChild(sel2);
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
