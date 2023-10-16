package com.howudodat;

import java.util.ArrayList;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.events.ColumnResizedEvent;
import org.dominokit.domino.ui.datatable.plugins.column.ColumnFilterMeta;
import org.dominokit.domino.ui.datatable.plugins.column.ColumnHeaderFilterPlugin;
import org.dominokit.domino.ui.datatable.plugins.column.ResizeColumnsPlugin;
import org.dominokit.domino.ui.datatable.plugins.filter.header.TextHeaderFilter;
import org.dominokit.domino.ui.datatable.plugins.header.HeaderBarPlugin;
import org.dominokit.domino.ui.datatable.plugins.row.RowClickPlugin;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.dialogs.Dialog;
import org.dominokit.domino.ui.elements.BaseElement;
import org.dominokit.domino.ui.grid.GridLayout;
import org.dominokit.domino.ui.grid.SectionSpan;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.layout.NavBar;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Popover;
import org.dominokit.domino.ui.style.Calc;
import org.dominokit.domino.ui.style.DominoCss;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.dominokit.domino.ui.utils.PostfixAddOn;
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
			ColumnHeaderFilterPlugin<tabledata> testColumnHeaderFilterPlugin = ColumnHeaderFilterPlugin.<tabledata>create();
			TableConfig<tabledata> tableConfig = new TableConfig<>();
			tableConfig.addColumn(
				ColumnConfig.<tabledata>create("Label", "Label")
					.applyMeta(ColumnFilterMeta.of(TextHeaderFilter.<tabledata>create()))
					.setCellRenderer(
						cell -> { return text(cell.getTableRow().getRecord().label); }))

				.setFixed(true)
				.setFixedBodyHeight(Calc.sub(Unit.vh.of(100), Unit.px.of(115)))
				.addPlugin(new ResizeColumnsPlugin<tabledata>().configure(config -> config.setClipContent(true)))
				.addPlugin(new RowClickPlugin<>(row -> { GWT.log("CLICKED:"+row.getRecord().label); }))
				.addPlugin(new HeaderBarPlugin<tabledata>("", "")
						.addActionElement(dataTable -> Icons.filter_menu_outline()
							.clickable()
							.addClickListener(evt -> {
								GWT.debugger();
								testColumnHeaderFilterPlugin.getFiltersRowElement().toggleDisplay(); })
							.element())
						.addActionElement(new HeaderBarPlugin.ClearSearch<>())
						.addActionElement(new HeaderBarPlugin.SearchTableAction<tabledata>()
							.withSearchBox(
								(parent, searchBox) -> {
									searchBox.addCss(dui_max_w_64, dui_bg_dominant_l_1, dui_rounded_md);
								}))
			)		
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

	public class DlgTest extends Dialog {
		public DlgTest() {
			init(this);
			this.setWidth("90%");
			this.setHeight(Calc.sub(Unit.vh.of(100), Unit.px.of(50)));
			initHeader();
			this.setModal(true);
			this.setAutoClose(false);
		}

	
		protected void initHeader() {
			MdiIcon icnSend = Icons.send();
			Popover pop = Popover.create(icnSend)
				.setCloseOnBlur(true)
				.closeOnEscape(true)
				.setPosition(DropDirection.BOTTOM_MIDDLE)
				.appendChild(Card.create("Card Title")
				.appendChild(Button.create("Test Me"))
				.addCss(dui_bg_accent, dui_fg, dui_elevation_0, dui_m_2px, dui_rounded_sm)
			);

			NavBar nav = NavBar.create().addCss(dui_dialog_nav)
				.setTitle("Test Dialog")
				.appendChild(PostfixAddOn.of(icnSend))
				.appendChild(PostfixAddOn.of((Icons.close().clickable().addClickListener(e -> close()))));


			getHeader().appendChild(nav);
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

		new DlgTest().open();
	}
}
