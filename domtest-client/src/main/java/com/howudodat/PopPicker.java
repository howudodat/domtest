package com.howudodat;

import static org.dominokit.domino.ui.style.DisplayCss.dui_elevation_0;
import static org.dominokit.domino.ui.style.SpacingCss.dui_m_2px;
import static org.dominokit.domino.ui.style.SpacingCss.dui_rounded_sm;
import static org.dominokit.domino.ui.style.SpacingCss.dui_whitespace_nowrap;
import static org.dominokit.domino.ui.utils.DomElements.dom;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.TableRow;
import org.dominokit.domino.ui.datatable.plugins.row.RowClickPlugin;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Popover;
import org.dominokit.domino.ui.utils.HasSelectionListeners.SelectionListener;

import elemental2.dom.HTMLElement;

public class PopPicker<T> extends Popover {
	protected LocalListDataStore<T> ds = new LocalListDataStore<>(); 
	protected DataTable<T> tbl = null;
	protected T selectedRow = null;
	protected SelectionListener<? super PopPicker<T>, ? super T> selectionListener = null;

	public PopPicker(IsElement<? extends HTMLElement> target, String title) {
		super(target.element());
		this
			.setOpenOnClick(true)
			.closeOnEscape(true)
			.setPosition(DropDirection.BEST_MIDDLE_DOWN_UP)
			.appendChild(Card.create(title)
				.appendChild(createChooser(this))
				.withHeader((crd, ch) -> {
					if (title == null || title.length() == 0)
					ch.hide();
				})
				.addCss(dui_elevation_0, dui_m_2px, dui_rounded_sm)
			);
	}

	public PopPicker<T> setData(List<T> list) {
		ds.setData(list);
		return this;
	}
	public PopPicker<T> setData(T[] list) {
		ds.setData(Arrays.asList(list));
		return this;
	}

	public void addSelectionListener(SelectionListener<? super PopPicker<T>, ? super T> selectionListener) {
		this.selectionListener = selectionListener;
	}

	public void setFilter(String filter) {
	}

	public T getSelectedObject() {
		return selectedRow;
	}

	public void setSelectedObject(T val) {
		tbl.pauseSelectionListeners();
		List<TableRow<T>> rows = tbl.getRows();
		for (TableRow<T> row : rows) {
			if(row.getRecord().equals(val)) { 
				row.select(); 
				selectedRow = row.getRecord();
				break; 
			}
		}
		tbl.resumeSelectionListeners();
	}

	protected DivElement t(String text) {
	    return  new DivElement(dom.div()).setTextContent(text);
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
							return t(o.toString()).addCss(dui_whitespace_nowrap).element();
					}))
			.addPlugin(new RowClickPlugin<>(row -> {
				pop.collapse();
				onClick(row.getRecord());
			}))
			.setMultiSelect(false)
		;
 
		tbl = new DataTable<>(tableConfig, ds);
		tbl.removeCss("dui-datatable-striped");
		tbl.headerElement().hide();

		return tbl;

	}

	protected void onClick(T tdata) {
		selectedRow = tdata;
		if (selectionListener != null) selectionListener.onSelectionChanged(Optional.of(this), tdata);
	}

}
