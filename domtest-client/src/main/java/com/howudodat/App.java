package com.howudodat;

import java.util.Comparator;
import java.util.List;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.DataTable.LocalRowFilter;
import org.dominokit.domino.ui.datatable.events.ColumnResizedEvent;
import org.dominokit.domino.ui.datatable.events.SearchEvent;
import org.dominokit.domino.ui.datatable.model.Category;
import org.dominokit.domino.ui.datatable.model.Filter;
import org.dominokit.domino.ui.datatable.plugins.column.ColumnFilterMeta;
import org.dominokit.domino.ui.datatable.plugins.column.ColumnHeaderFilterPlugin;
import org.dominokit.domino.ui.datatable.plugins.column.ResizeColumnsPlugin;
import org.dominokit.domino.ui.datatable.plugins.filter.header.TextHeaderFilter;
import org.dominokit.domino.ui.datatable.plugins.header.HeaderBarPlugin;
import org.dominokit.domino.ui.datatable.plugins.pagination.BodyScrollPlugin;
import org.dominokit.domino.ui.datatable.plugins.pagination.SortDirection;
import org.dominokit.domino.ui.datatable.plugins.pagination.SortPlugin;
import org.dominokit.domino.ui.datatable.plugins.row.RowClickPlugin;
import org.dominokit.domino.ui.datatable.plugins.selection.SelectionPlugin;
import org.dominokit.domino.ui.datatable.store.LocalListScrollingDataSource;
import org.dominokit.domino.ui.datatable.store.RecordsSorter;
import org.dominokit.domino.ui.datatable.store.SearchFilter;
import org.dominokit.domino.ui.dialogs.Dialog;
import org.dominokit.domino.ui.elements.BaseElement;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.forms.suggest.MultiSelect;
import org.dominokit.domino.ui.forms.suggest.SelectOption;
import org.dominokit.domino.ui.grid.GridLayout;
import org.dominokit.domino.ui.grid.SectionSpan;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.layout.NavBar;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.notifications.Notification;
import org.dominokit.domino.ui.notifications.Notification.Position;
import org.dominokit.domino.ui.popover.Popover;
import org.dominokit.domino.ui.style.Calc;
import org.dominokit.domino.ui.style.DominoCss;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import org.dominokit.domino.ui.utils.Unit;
import org.gwtproject.core.client.GWT;
import org.gwtproject.http.client.Request;
import org.gwtproject.http.client.RequestBuilder;
import org.gwtproject.http.client.RequestCallback;
import org.gwtproject.http.client.RequestException;
import org.gwtproject.http.client.Response;
import org.gwtproject.i18n.client.DateTimeFormat;

import com.google.gwt.core.client.EntryPoint;
import com.howudodat.App.DlgTest;
import com.howudodat.db.DBHome;
import com.howudodat.jsi.json.JHome;
import com.howudodat.jsi.json.JResults;

import elemental2.core.Global;
import elemental2.dom.HTMLDivElement;
import jsinterop.base.Js;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint, ElementsFactory, DominoCss {
	protected DataTable<JHome> tbl = null;
	protected LocalListScrollingDataSource<JHome> ds = new LocalListScrollingDataSource<>(100);
	protected AppLayout layout = AppLayout.create("Domino-ui test");
	protected int nTotalRecords = -1;

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
		protected DivElement content;
		protected DataTable<JHome> tbl = null;

		public PnlTableTest() {
			super(ElementsFactory.elements.div().element());
			content = div().addCss(dui_flex, dui_flex_col);
			tbl = createTable();
MultiSelect<String> select =  MultiSelect.<String>create("Country")
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
					

			content.appendChild(Button.create("test select").addClickListener(l->{
				GWT.log(select.getValue().toString());
			}));

			content.appendChild(tbl);

			this.appendChild(div()
				.addCss(dui_flex, dui_flex_col)
				.appendChild(content)
			);
		}

		protected DataTable<JHome> createTable() {
			ColumnHeaderFilterPlugin<JHome> testColumnHeaderFilterPlugin = ColumnHeaderFilterPlugin.<JHome>create();
			TableConfig<JHome> tableConfig = new TableConfig<>();
			tableConfig.addColumn(
				ColumnConfig.<JHome>create("Facility", "Facility")
					.sortable()
					.applyMeta(ColumnFilterMeta.of(TextHeaderFilter.<JHome>create()))
					.setCellRenderer(
						cell -> { return text(cell.getTableRow().getRecord().FacilityName); }))
			.addColumn(
				ColumnConfig.<JHome>create("Placed", "Placed")
					.applyMeta(ColumnFilterMeta.of(TextHeaderFilter.<JHome>create()))
					.setCellRenderer(
						cell -> { return text(""+cell.getTableRow().getRecord().placed); }))
			.addColumn(
				ColumnConfig.<JHome>create("Referred", "Referred")
					.applyMeta(ColumnFilterMeta.of(TextHeaderFilter.<JHome>create()))
					.setCellRenderer(
						cell -> { return text(""+cell.getTableRow().getRecord().referred); }))
			.addColumn(
				ColumnConfig.<JHome>create("View", "")
					.hide()
					.setCellRenderer(
						cell -> {
								return Icons.pencil().clickable().addClickListener(l->{
										Notification.create("Pencic Clicked Event").setPosition(Position.TOP_MIDDLE).show();
								}).element();
							}))
				
			.setFixed(true)
			.setFixedBodyHeight(Calc.sub(Unit.vh.of(100), Unit.px.of(175)))

			.addPlugin(new ResizeColumnsPlugin<JHome>().configure(config -> config.setClipContent(true)))
			.setMultiSelect(true)
			.addPlugin(testColumnHeaderFilterPlugin)
			.addPlugin(new BodyScrollPlugin<>())
			.addPlugin(new SelectionPlugin<>())
			.addPlugin(new SortPlugin<>())
			.addPlugin(new RowClickPlugin<>(row -> { 
				(new DlgTest()).open();
				tbl.getTableConfig().getColumnByName("View").show();
				GWT.log("CLICKED:"+row.getRecord().FacilityName); 
				Notification.create("Row Clicked Event").setPosition(Position.TOP_MIDDLE).show();
			}))

			.addPlugin(new HeaderBarPlugin<JHome>("", "")
				.addActionElement(dataTable -> Icons.filter_menu_outline()
					.clickable().addClickListener(evt -> {
						testColumnHeaderFilterPlugin.getFiltersRowElement().toggleDisplay(); 
					})
					.element())
				.addActionElement(new HeaderBarPlugin.ClearSearch<>())
				.addActionElement(new HeaderBarPlugin.SearchTableAction<JHome>()
					.withSearchBox(
						(parent, searchBox) -> {
							searchBox.addCss(dui_max_w_64, dui_bg_dominant_l_1, dui_rounded_md);
						}))
			)		
			;

			ds.setSearchFilter(new HomeSearchFilter());
			ds.setRecordsSorter(new HomeSorter());
			ds.onDataChanged((evt) -> {
				layout.getNavBar().setTitle(ds.getFiltered().size() + " / " + nTotalRecords);
			});
			
			tbl = new DataTable<>(tableConfig, ds);
			// tbl.removeCss("dui-datatable-striped");
			tbl.addTableEventListener(ColumnResizedEvent.COLUMN_RESIZED, evt -> {
				Notification.create("Resize Event").setPosition(Position.TOP_MIDDLE).show();
			});
			return tbl;
		}

		public class HomeSearchFilter implements SearchFilter<JHome> {

			@Override
			public boolean filterRecord(SearchEvent event, JHome record) {
				List<Filter> searchFilters = event.getByCategory(Category.SEARCH);
				if (searchFilters.isEmpty()) return false;

				if (event.getFilters().size() == 1 && event.getFilters().get(0).getFieldName() == "*")
					return foundByGlobalSearch(record, event.getFilters().get(0));

				for (Filter f : event.getFilters()) {

					switch(f.getFieldName()) {
						case "*":
							if (f.getValues().get(0).length() > 0 && !foundByGlobalSearch(record, f)) return false;
							break;
						case "Facility":
							if(!record.FacilityName.toLowerCase().contains(f.getValues().get(0).toLowerCase())) return false;
							break;
						case "Referred":
							if (!checkNumeric(record.referred, f.getValues().get(0))) return false;
							break;
						case "Placed":
							if (!checkNumeric(record.placed, f.getValues().get(0))) return false;
							break;
					}
				}
				
				return true;
			}

			private boolean checkNumeric(int val, String filter) {
				try {
					if (filter.startsWith(">=")) {
						int n = Integer.parseInt(filter.replace(">=", ""));
						return val >= n;
					} else if (filter.startsWith("<=")) {
						int n = Integer.parseInt(filter.replace("<=", ""));
						return val <= n;
					} else if (filter.startsWith(">")) {
						int n = Integer.parseInt(filter.replace(">", ""));
						return val > n;
					} else if (filter.startsWith("<")) {
						int n = Integer.parseInt(filter.replace("<", ""));
						return val < n;
					} else {
						int n = Integer.parseInt(filter.replace("=", ""));
						return val == n;
					}
				} catch (NumberFormatException e) {
					return true;
				}
			}

			private boolean foundByGlobalSearch(JHome record, Filter searchFilter) {
				String filter = searchFilter.getValues().get(0).toLowerCase();
				if (filter.length() == 0) return true;
				if (record.FacilityName.toLowerCase().contains(filter)) return true;
				
				return false;
			}
		}

		public class HomeSorter implements RecordsSorter<JHome> {
			@Override
			public Comparator<JHome> onSortChange(String sortBy, SortDirection sortDirection) {
				// final Comparator<JHome> c;
				
				switch (sortBy) {
				case "Facility":
					if (SortDirection.ASC.equals(sortDirection)) return (o1, o2) -> o2.FacilityName.compareToIgnoreCase(o1.FacilityName); 
					else if (SortDirection.DESC.equals(sortDirection)) return (o1, o2) -> o1.FacilityName.compareToIgnoreCase(o2.FacilityName); 
					break;
				// case "Client" :
				 	// c = Comparator.comparing(DBClient::toPickerString);
				// 	break;
				// case "Cur Location" :
				// 	c = Comparator.comparing(DBClient::getStrCurLoc);
				// 	break;
				// case "Source" :
				// 	c = Comparator.comparing(DBClient::getStrSource);
				// 	break;
				// case "Status" :
				// 	c = Comparator.comparing(DBClient::getStatus);
				// 	break;
				// case "Timeframe" :
				// 	c = Comparator.comparing(DBClient::getTimeFrame);
				// 	break;
				// case "Initial Contact" :
				// 	c = Comparator.comparing(DBClient::Initial_Contact);
				// 	break;
				// default:	// use the default sort from the db, which is Status
				// 	c = Comparator.comparingLong(DBClient::getStatus);
				}
				
				return null;
				// if (SortDirection.ASC.equals(sortDirection)) return c;
				// else if (SortDirection.DESC.equals(sortDirection)) return c.reversed();

//				return (o1, o2) -> Long.compare(o2.FU_Priority, o1.FU_Priority);
			}

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

			this.addBeforeExpandListener(() -> {
				GWT.log("Opening");
			});

			this.addBeforeCollapseListener(() -> {
				GWT.log("Closing");
			});

			this.addCollapseListener(l -> {
				GWT.log("Closed");
			});

			this.addExpandListener(l -> {
				GWT.log("Opened");
			});
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

	public String toJson(final Object obj) {
		return Global.JSON.stringify(obj, (key, value) -> {
			return (key.startsWith("_") ? Js.undefined() : value);
		});
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Results res = new Results();
		// Home home = new Home();
		// home.id = 1;
		// home.FacilityName = "Test Home";
		// home.setLocal();
		// home.lastupdate(new Date());

		// res.addHome(home);
		// res.success = true;

		// // com.howudodat.js.DBResults res = new DBResults();
		// GWT.log(toJson(res));
		// GWT.log(res.toJson());
		// GWT.log(home.toPickerString());
		// GWT.debugger();

		// String json = "{ \"id\":1,\"FacilityName\":\"Test Home\"}";
		// Object o = Global.JSON.parse(json);
		// Home hm = (Home)o;
		// GWT.log(hm.toPickerString());

		layout.getNavBar().appendChild(PostfixAddOn.of(Icons.test_tube().clickable().addClickListener(l->testJsonDecode(""))));
		layout.getNavBar().appendChild(PostfixAddOn.of(Icons.test_tube().clickable().addClickListener(l->testJsonDecode("100"))));
		layout.getNavBar().appendChild(PostfixAddOn.of(Icons.test_tube().clickable().addClickListener(l->testJsonDecode("500"))));
		layout.getNavBar().appendChild(PostfixAddOn.of(Icons.test_tube().clickable().addClickListener(l->testJsonDecode("1000"))));
		layout.getNavBar().appendChild(PostfixAddOn.of(Icons.test_tube().clickable().addClickListener(l->testJsonDecode("2000"))));
		layout.getNavBar().appendChild(PostfixAddOn.of(Icons.test_tube().clickable().addClickListener(l->testJsonDecode("4000"))));
		layout.withContent((parent1, content) -> {
			content.appendChild(new PnlTableTest());
		});

		body().appendChild(layout);
	}


	protected void testJsonDecode(String len) {
		GWT.log(new Date().toString() + " Retrieving from network");
		String url = "https://api.careplacement.com/api_v2/getHomes.php?cmd=all";
//		String url = "https://raw.githubusercontent.com/howudodat/domtest/main/testdata"+len+".json";
		time("json_test");
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		RequestCallback rcb = new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				if (200 == response.getStatusCode()) {
					timeLog("json_test", "Received from network len:" + response.getText().length());
					// Results res_jsi = new Results(response.getText());
					// if (res_jsi.success)
					// 	GWT.log(new Date().toString() + " validate jsi size:" + res_jsi.homes.size());

					// DBResults res_jack = DBResults_MapperImpl.INSTANCE.read(response.getText());
					// if (res_jack.success)
					// 	GWT.log(new Date().toString() + " validate jackson size:" + res_jack.homes.size());

					JResults res_j = JResults.Parse(response.getText());
					if (res_j.success) {
						timeLog("json_test", "Finished parsing record count:"+res_j.homes.length);
						nTotalRecords = res_j.homes.length;
						ds.setData(res_j.homes.asList());
						ds.load();
						timeLog("json_test", "Finished building Table");
//						layout.getNavBar().setTitle(res_j.homes.length + " / " + res_j.homes.length);
					}
					timeEnd("json_test");
				} else {
				}
			}
			
			@Override
			public void onError(Request request, Throwable exception) {
			}
		};
		
		try {
			builder.sendRequest("", rcb);
		} catch (RequestException e) {
		}
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
	
		public Date() { super(); }
		public String toString() {
			return sdfLong.format(this);
		}
	}
	}
