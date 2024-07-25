package com.howudodat;

import java.util.ArrayList;
import java.util.Random;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.plugins.pagination.BodyScrollPlugin;
import org.dominokit.domino.ui.datatable.plugins.pagination.BodyScrollPluginConfig;
import org.dominokit.domino.ui.datatable.store.LocalListScrollingDataSource;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.style.Calc;
import org.dominokit.domino.ui.style.DominoCss;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import org.dominokit.domino.ui.utils.Unit;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint, ElementsFactory, DominoCss {
	protected AppLayout layout = AppLayout.create("Domino-ui test");
	protected LocalListScrollingDataSource<JClient> ds = new LocalListScrollingDataSource<>(50);
	protected DataTable<JClient> tbl = null;
	protected ArrayList<JClient> data = new ArrayList<>();
	protected Random rand = new Random();
		 
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		createInitialData();

		layout.withContent((parent1, content) -> {
			content.appendChild(createPage());
		});

		layout.withNavBar((parent1, header) ->{
			header.appendChild(PostfixAddOn.of(Button.create("rebuild").addClickListener(l->onRebuild())));
			header.appendChild(PostfixAddOn.of(Button.create("single").addClickListener(l->onTimer())));
			header.appendChild(PostfixAddOn.of(Button.create("timer").addClickListener(l-> {
				Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
					@Override
					public boolean execute() {
						onTimer();
						return true;
					}
				}, 1000);
		
			})));
		});
		body().appendChild(layout);

	}

	protected DivElement createPage() {
		return ElementsFactory.elements.div().appendChild(createTable());
	}

	protected void onRebuild() {
		layout.getContent().clearElement()
			.appendChild(createPage());
	}

	protected ColumnConfig<JClient> createColumn(String col) {
		switch (col) {
			case "LastName":
			return ColumnConfig.<JClient>create("LastName", "LastName")
				.sortable()
				.setCellRenderer(cell -> {
					return text(cell.getRecord().LastName);
				});	 
			case "First1":
			return ColumnConfig.<JClient>create("First1", "First1")
				.sortable()
				.setCellRenderer(cell -> {
					return text(cell.getRecord().First1);
				});	 
			case "First2":
			return ColumnConfig.<JClient>create("First2", "First2")
				.sortable()
				.setCellRenderer(cell -> {
					return text(cell.getRecord().First2);
				});	 
			case "Age":
			return ColumnConfig.<JClient>create("Age", "Age")
				.sortable()
				.setCellRenderer(cell -> {
					return text(""+cell.getRecord().Age);
				});	 
			case "Weight":
			return ColumnConfig.<JClient>create("Weight", "Weight")
				.sortable()
				.setCellRenderer(cell -> {
					return text(""+cell.getRecord().Weight);
				});	 


			default:
			return ColumnConfig.<JClient>create(col, col).setCellRenderer( cell -> { return text(col); });
		}
	}

	protected DataTable<JClient> createTable() {
		String arrChoices[] = { "LastName", "First1", "First2", "Age", "Weight" };

		TableConfig<JClient> tableConfig = new TableConfig<>();
		tableConfig.setFixed(true);
		tableConfig.addColumn(ColumnConfig.<JClient>create("Refer", "")
			.setCellRenderer(
				cell -> {
					MdiIcon icnSend = Icons.send();
					PopPicker<JClient> pop = new PopPicker<>(icnSend, "Please Select a Client");
					pop.addSelectionListener((pe, val) -> onRefer(val, cell.getRecord()));
					return icnSend.element();
		}));

		for (String col : arrChoices) 
			tableConfig.addColumn(createColumn(col));

		tableConfig.setFixed(true)
			.setFixedBodyHeight(Calc.sub(Unit.vh.of(100), Unit.px.of(110)))
			.setMultiSelect(false)
			.addPlugin((new BodyScrollPlugin<JClient>()).setConfig(new BodyScrollPluginConfig(25)))
		;


		tbl = new DataTable<>(tableConfig, ds);
		tbl.removeCss("dui-datatable-striped");
		ds.setData(data);
		ds.load();
		return tbl;
	}

	protected void createInitialData() {
 		for (int x=0;x<20;x++) {
			JClient c = new JClient();
			c.LastName = "Last Name " + x;
			c.First1 = "First " + x;
			c.Age = rand.nextInt(150);
			c.Weight = rand.nextInt(400);
			data.add(c);
		}
	}

	protected void onTimer() {
		int ndx = rand.nextInt(20);
		JClient c = data.get(ndx);
		c.First2 = "C : " + ++c.changed;
		c.Age = rand.nextInt(150);
		c.Weight = rand.nextInt(400);

		ds.updateRecord(c);
		GWT.log(c.First1 + ":" + c.First2);
	}

	protected void onPreview(JClient client) {}
	protected void onRefer(JClient c1, JClient c2) {}

}
