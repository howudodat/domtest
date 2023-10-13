package com.howudodat;

import java.util.ArrayList;

import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.events.ColumnResizedEvent;
import org.dominokit.domino.ui.datatable.plugins.column.ResizeColumnsPlugin;
import org.dominokit.domino.ui.datatable.plugins.row.RowClickPlugin;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.elements.BaseElement;
import org.dominokit.domino.ui.grid.GridLayout;
import org.dominokit.domino.ui.grid.SectionSpan;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.style.Calc;
import org.dominokit.domino.ui.style.DominoCss;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.dominokit.domino.ui.utils.Unit;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import elemental2.dom.HTMLDivElement;
import jsinterop.base.Js;

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

	protected class tabledata {
		public String label; 
		public tabledata (String label) { this.label = label; }
	}

	public class PnlTableTest extends BaseElement<HTMLDivElement, PnlTableTest>  {

		protected LocalListDataStore<tabledata> ds = new LocalListDataStore<>();

		public PnlTableTest() {
			super(ElementsFactory.elements.div().element());
			this.appendChild(div()
				.addCss(dui_flex, dui_flex_col)
				.appendChild(createTable())
			);
			
			ArrayList<tabledata> alData = new ArrayList<>();
			for (int x=0; x<10; x++)
				alData.add(new tabledata("Test "+x));
			ds.setData(alData);
		}

		protected DataTable<tabledata> createTable() {
			TableConfig<tabledata> tableConfig = new TableConfig<>();
			tableConfig.addColumn(
				ColumnConfig.<tabledata>create("Label", "Label")
					.setCellRenderer(
						cell -> { return text(cell.getTableRow().getRecord().label); }))

				.setFixed(true)
				.setFixedBodyHeight(Calc.sub(Unit.vh.of(100), Unit.px.of(115)))
				.addPlugin(new ResizeColumnsPlugin<tabledata>().configure(config -> config.setClipContent(true)))
				.addPlugin(new RowClickPlugin<>(row -> { GWT.log("CLICKED:"+row.getRecord().label); }))
			;

			DataTable<tabledata> tbl = new DataTable<>(tableConfig, ds);
			tbl.removeCss("dui-datatable-striped");
			tbl.addTableEventListener(ColumnResizedEvent.COLUMN_RESIZED, evt -> {
				ColumnResizedEvent event = Js.uncheckedCast(evt);
				GWT.log(evt.getType() + " - " + evt.toString());
				GWT.log(event.getColumn().getTitle() + " = " + event.getSizeDiff());
			});
			return tbl;
		}
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		var layout = AppLayout.create("Domino-ui test");

		layout.withContent((parent1, content) -> {
			content.appendChild(new PnlTableTest());
		});

		body().appendChild(layout);
	}
}
