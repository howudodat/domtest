package com.howudodat;
import java.util.Arrays;
import java.util.List;

import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.TableRow;
import org.dominokit.domino.ui.datatable.events.SearchEvent;
import org.dominokit.domino.ui.datatable.model.Category;
import org.dominokit.domino.ui.datatable.model.Filter;
import org.dominokit.domino.ui.datatable.plugins.row.RowClickPlugin;
import org.dominokit.domino.ui.datatable.plugins.selection.SelectionPlugin;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.datatable.store.SearchFilter;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Popover;
import org.dominokit.domino.ui.utils.ElementsFactory;

import com.google.gwt.core.client.GWT;

import elemental2.dom.HTMLElement;
class PopPicker<T> extends Popover {
	protected LocalListDataStore<T> ds = new LocalListDataStore<>();
	protected DataTable<T> tbl = null;

	public PopPicker(IsElement<? extends HTMLElement> target) {
		super(target.element());
		this
				.setOpenOnClick(false)
				.closeOnEscape(true)
				.setPosition(DropDirection.BEST_MIDDLE_DOWN_UP)
				.appendChild(Card.create()
						.appendChild(createChooser(this))
						.withHeader((crd, ch) -> { ch.hide(); }))

				.addExpandListener(() -> {
					GWT.log("Expand Listener");
					clearFilter();
				});
	}

	public PopPicker<T> setData(T[] list) {
		ds.setData(Arrays.asList(list));
		return this;
	}

	public void clearFilter() {
		GWT.log("Clear Filter - ");
		tbl.getSearchContext().clear();
		tbl.getSearchContext().fireSearchEvent();
	}

	public void setSelectedObject(T val) {
		tbl.pauseSelectionListeners();
		List<TableRow<T>> rows = tbl.getRows();
		for (TableRow<T> row : rows) {
			if (row.getRecord().equals(val)) {
				row.select();
				break;
			}
		}
		tbl.resumeSelectionListeners();
	}

	protected DataTable<T> createChooser(Popover pop) {
		TableConfig<T> tableConfig = new TableConfig<>();
		tableConfig
				.setFixed(false)
				.addColumn(ColumnConfig.<T>create("x", "x")
						.sortable()
						.setCellRenderer(
								cell -> {
									Object o = cell.getTableRow().getRecord();
									return ElementsFactory.elements.text(o.toString());
								}))
				.addPlugin(new RowClickPlugin<>(row -> {
					GWT.log("Row Click");
					row.select();
					pop.collapse();
				}))
				.setMultiSelect(false)
				.addPlugin(new SelectionPlugin<>());

		ds.setSearchFilter(new PickerSearchFilter());
		tbl = new DataTable<>(tableConfig, ds);
		tbl.removeCss("dui-datatable-striped");
		tbl.headerElement().hide();
		tbl.addSelectionListener((selectedTableRows, selectedRecords) -> {
			GWT.log("Selection Listener");
		});

		return tbl;

	}

	public class PickerSearchFilter implements SearchFilter<T> {

		@Override
		public boolean filterRecord(SearchEvent event, T record) {
			List<Filter> searchFilters = event.getByCategory(Category.SEARCH);
			if (searchFilters.isEmpty())
				return true;

			if (event.getFilters().size() == 1 && event.getFilters().get(0).getFieldName() == "*")
				return foundByGlobalSearch(record, event.getFilters().get(0));
			return true;
		}

		private boolean foundByGlobalSearch(T record, Filter searchFilter) {
			String filter = searchFilter.getValues().get(0).toLowerCase();
			if (filter.length() == 0)
				return true;

			if (record.toString().toLowerCase().contains(filter))
				return true;
			return false;
		}
	}
}
