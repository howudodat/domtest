package com.howudodat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import org.dominokit.domino.ui.utils.HasSelectionListeners.SelectionListener;

import com.google.gwt.core.client.GWT;

import elemental2.dom.HTMLElement;
class PopPicker<T> extends Popover {
	protected LocalListDataStore<T> ds = new LocalListDataStore<>();
	protected DataTable<T> tbl = null;
	protected T selectedRow = null;
	protected SelectionListener<? super PopPicker<T>, ? super T> selectionListener = null;

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

	public void setSelectionListener(SelectionListener<? super PopPicker<T>, ? super T> selectionListener) {
		this.selectionListener = selectionListener;
	}

	public void setFilter(String filter) {
		tbl.getSearchContext().clear();
		tbl.getSearchContext().add(Filter.create("*", filter, Category.SEARCH));
		tbl.getSearchContext().fireSearchEvent();

		tbl.pauseSelectionListeners();
		List<T> filteredRecords = ds.getFilteredRecords();
		if (filteredRecords.size() == 1) {
			List<TableRow<T>> rows = tbl.getRows();
			for (TableRow<T> row : rows) {
				if (row.isVisible()) {
					row.select();
					break;
				}
			}
		} else if (filteredRecords.size() > 1 && selectedRow != null) {
			setSelectedObject(selectedRow);
		} else if (filteredRecords.size() == 0) {
			tbl.deselectAll();
			selectedRow = null;
		}
		tbl.resumeSelectionListeners();
	}

	public void clearFilter() {
		GWT.log("Clear Filter - ");
		tbl.getSearchContext().clear();
		tbl.getSearchContext().fireSearchEvent();
		if (selectedRow != null)
			setSelectedObject(selectedRow);
	}

	public T getSelectedObject() {
		return selectedRow;
	}

	public void setSelectedObject(T val) {
		tbl.pauseSelectionListeners();
		List<TableRow<T>> rows = tbl.getRows();
		for (TableRow<T> row : rows) {
			if (row.getRecord().equals(val)) {
				row.select();
				selectedRow = row.getRecord();
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
					pop.collapse();
					GWT.log("Row Click");
					row.select();
					pop.collapse();
					onClick(row.getRecord());
				}))
				.setMultiSelect(false)
				.addPlugin(new SelectionPlugin<>());

		ds.setSearchFilter(new PickerSearchFilter());
		tbl = new DataTable<>(tableConfig, ds);
		tbl.removeCss("dui-datatable-striped");
		tbl.headerElement().hide();
		tbl.addSelectionListener((selectedTableRows, selectedRecords) -> {
			GWT.log("Selection Listener");
			// pop.collapse();
			// selectedRow = selectedRecords.get(0).getRecord();
			// onClick(selectedRow);
		});

		return tbl;

	}

	protected void onClick(T tdata) {
		selectedRow = tdata;
		if (selectionListener != null)
			selectionListener.onSelectionChanged(Optional.of(this), tdata);
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
