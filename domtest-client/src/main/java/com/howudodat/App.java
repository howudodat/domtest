package com.howudodat;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.elements.BaseElement;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.forms.suggest.MultiSelect;
import org.dominokit.domino.ui.forms.suggest.SelectOption;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.notifications.Notification;
import org.dominokit.domino.ui.style.DominoCss;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.gwtproject.core.client.GWT;
import org.gwtproject.i18n.client.DateTimeFormat;

import com.google.gwt.core.client.EntryPoint;

import elemental2.dom.HTMLDivElement;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint, ElementsFactory, DominoCss {
	protected AppLayout layout = AppLayout.create("Domino-ui test");

	public class PnlSelectTest extends BaseElement<HTMLDivElement, PnlSelectTest> {

		public PnlSelectTest() {
			super(ElementsFactory.elements.div().element());

			DivElement content = div().addCss(dui_flex, dui_flex_col);

			MultiSelect<String> select = MultiSelect.<String>create("Country")
					.appendChild(SelectOption.create("USA", "USA", "America (USA)"))
					.appendChild(SelectOption.create("ARG", "ARG", "Argentina"))
					.appendChild(SelectOption.create("BRA", "BRA", "Brazil"))
					.appendChild(SelectOption.create("DEN", "DEN", "Denmark"))
					.appendChild(SelectOption.create("CRO", "CRO", "Croatia"))
					.appendChild(SelectOption.create("IND", "IND", "India"))
					.appendChild(SelectOption.create("SPA", "SPA", "Spain"))
					.appendChild(SelectOption.create("FRA", "FRA", "France"))
					.appendChild(SelectOption.create("JOR", "JOR", "Jordan"))
					.selectAt(0)
					.addChangeListener(
							(oldValue, newValue) -> {
								Notification.create("Item selected [ " + newValue + " ]")
										.show();
							});

			content.appendChild(select);
			// select.pauseChangeListeners();
			select.selectByValue("BRA", true);
			select.selectByValue("IND", true);

			content.appendChild(Button.create("test select").addClickListener(l -> {
				GWT.log(select.getValue().toString());
			}));

			this.appendChild(div()
					.addCss(dui_flex, dui_flex_col)
					.appendChild(content));
		}
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		layout.withContent((parent1, content) -> {
			content.appendChild(new PnlSelectTest());
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

	public static DateTimeFormat sdfLong = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss.SSS");

	public class Date extends java.util.Date {

		public Date() {
			super();
		}

		public String toString() {
			return sdfLong.format(this);
		}
	}
}
