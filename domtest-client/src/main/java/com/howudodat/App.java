package com.howudodat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.events.RecordDroppedEvent;
import org.dominokit.domino.ui.datatable.events.SearchEvent;
import org.dominokit.domino.ui.datatable.model.Category;
import org.dominokit.domino.ui.datatable.model.Filter;
import org.dominokit.domino.ui.datatable.plugins.DragDropPlugin;
import org.dominokit.domino.ui.datatable.plugins.header.HeaderBarPlugin;
import org.dominokit.domino.ui.datatable.plugins.row.RowClickPlugin;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.datatable.store.SearchFilter;
import org.dominokit.domino.ui.elements.BaseElement;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.style.DominoCss;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.gwtproject.i18n.client.DateTimeFormat;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import elemental2.dom.HTMLDivElement;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint, ElementsFactory, DominoCss {
	protected AppLayout layout = AppLayout.create("Domino-ui test");
	protected LocalListDataStore<String> ds = new LocalListDataStore<>();
	public static DateTimeFormat sdfLongMs = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss.SSS");

	public class PnlSelectTest extends BaseElement<HTMLDivElement, PnlSelectTest> {

		public PnlSelectTest() {
			super(ElementsFactory.elements.div().element());
			ArrayList<String> alStrings = new ArrayList<>();
			for (int x = 0; x < 1000; x++)
				alStrings.add("Item " + x);

			TableConfig<String> tableConfig = new TableConfig<>();
			tableConfig
					.setFixed(true)
					.addColumn(
							ColumnConfig.<String>create("title", "title")
									.setCellRenderer(cell -> {
										return text("test");
									})
									.setWidth("100%"))
					.addColumn(
							ColumnConfig.<String>create("Sel", "Sel")
									.sortable()
									.setCellRenderer(cell -> {
										return text("test");
									})
					)
					.addPlugin(new RowClickPlugin<>(row -> {
						row.updateRow();
					}))
					.addPlugin(
							new HeaderBarPlugin<String>("", "")
									.addActionElement(
											new HeaderBarPlugin.SearchTableAction<String>()
													.withSearchBox(
															(parent, searchBox) -> {
																searchBox.addCss(dui_max_w_64, dui_bg_dominant_l_1,
																		dui_rounded_md);
															})))
				;
												
			tableConfig.addPlugin(new DragDropPlugin<>());

			DataTable<String> tbl = new DataTable<>(tableConfig, ds);
			tbl.removeCss("dui-datatable-striped")
					.setCondensed(true)
					.headerElement().hide();
			tbl.addSelectionListener((selectedTableRows, selectedRecords) -> {
			});
			tbl.addTableEventListener(RecordDroppedEvent.RECORD_DROPPED, evt -> {
			});

			ds.setSearchFilter(new WrapperSearchFilter());

			DivElement content = div().addCss(dui_flex, dui_flex_col).appendChild(tbl);

			this.appendChild(div()
					.addCss(dui_flex, dui_flex_col)
					.appendChild(content));

			// timing starts here
			GWT.log(sdfLongMs.format(new Timestamp(System.currentTimeMillis())) + " Starting dataaset load");
			time("table");
			timeLog("table", "Starting data load");
			ds.setData(alStrings);
			timeEnd("table");
			GWT.log(sdfLongMs.format(new Timestamp(System.currentTimeMillis())) + " Finished dataaset load");
		}

		public class WrapperSearchFilter implements SearchFilter<String> {
			@Override
			public boolean filterRecord(SearchEvent event, String record) {
				List<Filter> searchFilters = event.getByCategory(Category.SEARCH);
				if (searchFilters.isEmpty()) return false;
				
				return searchFilters.isEmpty() || foundBySearch(record, searchFilters.get(0));
			}

			private boolean foundBySearch(String record, Filter searchFilter) {
				String filter = searchFilter.getValues().get(0).toLowerCase();
				if (record.toString().toLowerCase().contains(filter.toLowerCase())) return true;
				
				return false;
			}
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
}
