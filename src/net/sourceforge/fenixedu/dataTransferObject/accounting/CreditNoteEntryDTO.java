package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.util.Money;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class CreditNoteEntryDTO implements Serializable {

	private boolean selected;

	private Entry entry;

	private Money amountToPay;

	public CreditNoteEntryDTO(final Entry entry) {

		setEntry(entry);
		setAmountToPay(entry.getAmountWithAdjustment());
	}

	public Money getTotalAmount() {
		return getEntry().getAmountWithAdjustment();
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Money getAmountToPay() {
		return amountToPay;
	}

	public void setAmountToPay(Money amountToPay) {
		this.amountToPay = amountToPay;
	}

	public Entry getEntry() {
		return this.entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public LabelFormatter getDescription() {
		return getEntry().getDescription();
	}

}
