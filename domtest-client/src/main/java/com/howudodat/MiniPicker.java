package com.howudodat;

import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.utils.BaseDominoElement;

import elemental2.dom.HTMLElement;

public class MiniPicker<T> extends BaseDominoElement<HTMLElement, MiniPicker<T>> {
	TextBox el = TextBox.create();
	PopPicker<T> pop = new PopPicker<>(el.getInputElement());

	public static <T> MiniPicker<T> create(T options[]) {
		MiniPicker<T> p = new MiniPicker<>();
		p.createOptions(options);
		return p;
	}

	public MiniPicker() {
		// when we get focus we should show the popover
		el.getInputElement().addEventListener(EventType.focus, evt -> onFocus());
	}

	public void createOptions(T options[]) {
		pop.setData(options);
	}

	protected void onFocus() {
		if (pop.isCollapsed())
			pop.open();
		el.getInputElement().element().select();
	}

	@Override
	public HTMLElement element() {
		return el.element();
	}

	protected void onChange(T newval, boolean bQueit) {
		if (newval == null)
			el.withValue("", true);
		else
			el.withValue(newval.toString(), true);
	}

	public void setSelectedObject(T val) {
		pop.setSelectedObject(val);
		onChange(val, true);
	}
}