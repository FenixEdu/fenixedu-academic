package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Receipt;

public class CreateCreditNoteBean implements Serializable {

    private List<CreditNoteEntryDTO> creditNoteEntryDTOs;

    private DomainReference<Receipt> receipt;

    public CreateCreditNoteBean(final Receipt receipt) {
	setReceipt(receipt);
	setCreditNoteEntryDTOs(buildCreditNoteEntryDTOs(receipt));
    }

    public Receipt getReceipt() {
	return (this.receipt != null) ? this.receipt.getObject() : null;
    }

    public void setReceipt(Receipt receipt) {
	this.receipt = (receipt != null) ? new DomainReference<Receipt>(receipt) : null;
    }

    public List<CreditNoteEntryDTO> getCreditNoteEntryDTOs() {
	return creditNoteEntryDTOs;
    }

    public void setCreditNoteEntryDTOs(List<CreditNoteEntryDTO> creditNoteEntryDTOs) {
	this.creditNoteEntryDTOs = creditNoteEntryDTOs;
    }

    private List<CreditNoteEntryDTO> buildCreditNoteEntryDTOs(final Receipt receipt) {
	final List<CreditNoteEntryDTO> result = new ArrayList<CreditNoteEntryDTO>();
	for (final Entry entry : receipt.getReimbursableEntries()) {
	    result.add(new CreditNoteEntryDTO(entry));
	}

	return result;

    }

    public List<CreditNoteEntryDTO> getSelectedEntries() {
	final List<CreditNoteEntryDTO> result = new ArrayList<CreditNoteEntryDTO>();

	for (final CreditNoteEntryDTO creditNoteEntryDTO : getCreditNoteEntryDTOs()) {
	    if (creditNoteEntryDTO.isSelected()) {
		result.add(creditNoteEntryDTO);
	    }
	}

	return result;
    }

}
