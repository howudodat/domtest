package com.howudodat;

import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.grid.GridLayout;
import org.dominokit.domino.ui.grid.SectionSpan;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.style.Calc;
import org.dominokit.domino.ui.style.DominoCss;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.dominokit.domino.ui.utils.Unit;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint, ElementsFactory, DominoCss {

	public class PnlGridTest extends GridLayout {
		public PnlGridTest() {
			this.setCssProperty("grid-column-gap", "5px")
					.setCssProperty("grid-row-gap", "0px")
					.setLeftSpan(SectionSpan._1, true, true)
					.setHeaderSpan(SectionSpan._1);

			this.getLeftElement()
					.appendChild(Card.create("LEFT"))
					.setHeight(Calc.sub(Unit.vh.of(100), Unit.px.of(130)))
					.setWidth("250px");

			this.getHeaderElement()
					.appendChild(Card.create("TOP"))
					.show();

			this.getContentElement().appendChild(Card.create("BODY"));
		}
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		var layout = AppLayout.create("Domino-ui test");

		layout.withContent((parent1, content) -> {
			content.appendChild(new PnlGridTest());
		});

		body().appendChild(layout);
	}
}
