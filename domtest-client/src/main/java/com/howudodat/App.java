package com.howudodat;

import java.util.ArrayList;
import java.util.List;

import org.dominokit.domino.ui.data.DataStore;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.TableMode;
import org.dominokit.domino.ui.datatable.events.SearchEvent;
import org.dominokit.domino.ui.datatable.model.Category;
import org.dominokit.domino.ui.datatable.model.Filter;
import org.dominokit.domino.ui.datatable.plugins.column.ColumnFilterMeta;
import org.dominokit.domino.ui.datatable.plugins.column.ColumnHeaderFilterPlugin;
import org.dominokit.domino.ui.datatable.plugins.filter.header.TextHeaderFilter;
import org.dominokit.domino.ui.datatable.plugins.pagination.BodyScrollPlugin;
import org.dominokit.domino.ui.datatable.plugins.pagination.BodyScrollPluginConfig;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.datatable.store.LocalListScrollingDataSource;
import org.dominokit.domino.ui.datatable.store.SearchFilter;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.splitpanel.SplitPanel;
import org.dominokit.domino.ui.splitpanel.VSplitPanel;
import org.dominokit.domino.ui.style.Calc;
import org.dominokit.domino.ui.style.DominoCss;
import org.dominokit.domino.ui.tabs.Tab;
import org.dominokit.domino.ui.tabs.TabsPanel;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.dominokit.domino.ui.utils.Unit;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint, ElementsFactory, DominoCss {
	protected AppLayout layout = AppLayout.create("Domino-ui test").addCss(dui_h_full);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		layout.withContent((parent, content) -> {
			content.addCss(dui_h_full, dui_flex, dui_flex_col);
			content.appendChild(createPage());
		});

		body().appendChild(layout);
	}


	protected DivElement createPage() {
		return ElementsFactory.elements.div().appendChild(buildTabSingle());
	}

	protected DivElement createSplit() {
		DivElement mycontent = div();
		mycontent.addCss(dui_flex, dui_flex_col, dui_border_1, dui_border_solid, dui_border_black);

		SplitPanel pnlTop = SplitPanel.create().appendChild(p("Top"));
		SplitPanel pnlBottom = SplitPanel.create().appendChild(buildTabSingle());
		// pnlTop.addCss("auto-v-scroll-panel").setHeight("40%").addCss(dui_overflow_y_scroll);
		pnlTop.setHeight("40%");
		pnlBottom.setHeight("60%");

		VSplitPanel pnl = VSplitPanel.create()
				.appendChild(pnlTop)
				.appendChild(pnlBottom)
				.setPadding("0px")
				.setHeight(Calc.sub(Unit.vh.of(100), Unit.px.of(125)));
		mycontent.appendChild(pnl);
		return mycontent;
	}

	protected DivElement createSimpleDiv() {
		DivElement mycontent = div();
		mycontent.addCss(dui_flex, dui_flex_col, dui_border_1, dui_border_solid, dui_border_black); // Make height = viewport - header (e.g. 125px)
		mycontent.setHeight(Calc.sub(Unit.vh.of(100),Unit.px.of(125))); 
		mycontent.appendChild(buildTable(TableMode.AUTO, true));
		return mycontent;
	}

	protected TabsPanel buildTabAll() {
		return TabsPanel.create()
				// .appendChild(Tab.create("Default").addCss(dui_h_full).appendChild(buildTable(TableMode.DEFAULT, false)))
				.appendChild(Tab.create("Fixed").addCss(dui_h_full).appendChild(buildTable(TableMode.FIXED_HEIGHT, false)))
				.appendChild(Tab.create("Auto").addCss(dui_h_full).appendChild(buildTable(TableMode.AUTO, false)))
				// .appendChild(Tab.create("Default - Scroll").addCss(dui_h_full).appendChild(buildTable(TableMode.DEFAULT, true)))
				.appendChild(Tab.create("Fixed - Scroll").addCss(dui_h_full).appendChild(buildTable(TableMode.FIXED_HEIGHT, true)))
				.appendChild(Tab.create("Auto - Scroll").addCss(dui_h_full).appendChild(buildTable(TableMode.AUTO, true)))
				.addCss(dui_border_1, dui_border_solid, dui_border_red).setHeight(Calc.sub(Unit.vh.of(100), Unit.px.of(125))).withEachTab((panel, tab) -> {
					tab.getTabPanel().addCss(dui_h_full, dui_border_1, dui_border_solid, dui_border_yellow);
				});
	}

	protected TabsPanel buildTabSingle() {
		TabsPanel tabs = TabsPanel.create()
			.appendChild(Tab.create("Auto").addCss(dui_h_full).appendChild(buildTable(TableMode.AUTO, true)))
			// .appendChild(Tab.create("1").addCss(dui_h_full).appendChild(p("Test")))
			.addCss(dui_border_1, dui_border_solid, dui_border_red)
			// .setHeight(Calc.sub(Unit.vh.of(100), Unit.px.of(125)))
			;

		tabs.withTabsContent((t, content) -> {
			content.addCss(dui_flex, dui_flex_col, dui_h_full);
		});

		// Optional: ensure each tab panel also stretches
		tabs.withEachTab((panel, tab) -> {
			panel.addCss(dui_flex, dui_flex_col, dui_h_full);
			tab.getTabPanel().addCss(dui_flex, dui_flex_col, dui_h_full);
		});

		return tabs;
	}

	protected TabsPanel buildTab2() {
		return TabsPanel.create()
			.appendChild(Tab.create("Default").appendChild(buildTable(TableMode.DEFAULT, false)))
			.appendChild(Tab.create("Fixed").appendChild(buildTable(TableMode.FIXED_HEIGHT, false)))
			.appendChild(Tab.create("Auto").appendChild(buildTable(TableMode.AUTO, false)).addCss(dui_border_1, dui_border_solid, dui_border_yellow))
			.addCss(dui_border_1, dui_border_solid, dui_border_red, dui_h_full)
			;
	}

	protected TabsPanel buildTab1() {
		return TabsPanel.create()
			.appendChild(Tab.create("Default - Scroll").appendChild(buildTable(TableMode.DEFAULT, true)))
			.appendChild(Tab.create("Fixed - Scroll").appendChild(buildTable(TableMode.FIXED_HEIGHT, true)))
			.appendChild(Tab.create("Auto - Scroll").appendChild(buildTable(TableMode.AUTO, true)).addCss(dui_border_1, dui_border_solid, dui_border_yellow))
			.addCss(dui_border_1, dui_border_solid, dui_border_red, dui_h_full)
			;
	}

	class Person {
		public String name;
		public int age;
		public String notes;

		public Person() {}
		public Person(String name, int age) {
			this.name = name;
			this.age = age;
			notes = "Quis pharetra a pharetra fames blandit. Risus faucibus velit Risus imperdiet mattis neque volutpat, etiam lacinia netus dictum magnis per facilisi sociosqu. Volutpat. Ridiculus nostra.";
		}

		public Person name(String name) {
			this.name = name;
			return this;
		}

		public Person age(int age) {
			this.age = age;
			return this;
		}
	}

	protected ColumnHeaderFilterPlugin<Person> chfp = ColumnHeaderFilterPlugin.<Person>create();
	protected DataTable<Person> buildTable(TableMode type, boolean scroll) {
		TableConfig<Person> tableConfig = new TableConfig<>();
		tableConfig
			.addColumn(ColumnConfig.<Person>create("Name", "Name")
				.applyMeta(ColumnFilterMeta.of(TextHeaderFilter.<Person>create()))
				.setRenderer(cell -> cell.appendChild(p(cell.getRecord().name).addCss(dui_overflow_clip)))
				.setWidth("50px"))
			.addColumn(ColumnConfig.<Person>create("Age", "Age")
				.applyMeta(ColumnFilterMeta.of(TextHeaderFilter.<Person>create()))
				.setRenderer(cell -> cell.appendChild(p(""+cell.getRecord().age))))
			.addColumn(ColumnConfig.<Person>create("Notes", "Notes")
				.applyMeta(ColumnFilterMeta.of(TextHeaderFilter.<Person>create()))
				.setRenderer(cell -> cell.appendChild(p(cell.getRecord().notes))))

			.addPlugin(chfp)
			.addPlugin(new BodyScrollPlugin<Person>().setConfig(new BodyScrollPluginConfig(3)))
		;

		tableConfig
			.setTableMode(type)
			// .setFixedBodyHeight(Calc.sub(Unit.vh.of(100), Unit.px.of(200)))
		;

		ArrayList<Person> list = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			list.add(new Person("Name:" + i, i));
		}

		LocalListScrollingDataSource<Person> dsScroll = new LocalListScrollingDataSource<>(25);
		LocalListDataStore<Person> dsNormal = new LocalListDataStore<>();

		DataStore<Person> ds = null;
		if (scroll) {
			dsScroll.setData(list);
			dsScroll.setSearchFilter(new PersonSearchFilter());
			ds = dsScroll;
		} else { 
			dsNormal.setData(list);
			dsNormal.setSearchFilter(new PersonSearchFilter());
			ds = dsNormal;
		}

		
		DataTable<Person> tbl = new DataTable<>(tableConfig, ds);
		tbl.addCss(dui_border_1, dui_border_solid, dui_border_green);
		tbl.addCss(dui_flex_1);
		ds.load();
		return tbl;
	}

	public class PersonSearchFilter implements SearchFilter<Person> {
		@Override
		public boolean filterRecord(SearchEvent event, Person record) {
			List<Filter> searchFilters = event.getByCategory(Category.HEADER_FILTER);
			if (searchFilters.isEmpty())
				return true;

			boolean bFilter = true;
			for (Filter flt : searchFilters) {
				String filter = flt.getValues().get(0).toLowerCase();
				if (flt.getFieldName().equals("*"))
					bFilter = foundBySearch(record, flt);
				else if (flt.getFieldName().equals("Name"))
					bFilter = (record.name.toLowerCase().contains(filter.toLowerCase()));
				// else if (flt.getFieldName().equals("Age"))
				// 	bFilter = (record.age.toLowerCase().contains(flt.getValues().get(0).toLowerCase()));
				else if (flt.getFieldName().equals("Notes"))
					bFilter = (record.notes.toLowerCase().contains(flt.getValues().get(0).toLowerCase()));
				if (!bFilter)
					return false; // if any filter fails, we fail
			}
			return bFilter;
		}

		private boolean foundBySearch(Person record, Filter searchFilter) {
			return false;
		}
	}
}
