package net.sourceforge.fenixedu.presentationTier.docs.accounting;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.CreditNoteEntry;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import pt.utl.ist.fenix.tools.resources.IMessageResourceProvider;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CreditNoteDocument extends FenixReport {

    private CreditNote creditNote;

    private IMessageResourceProvider messageResourceProvider;

    private String basePath;

    private boolean original;

    public static class CreditNoteDocumentEntry {
	private String description;

	private String amount;

	public CreditNoteDocumentEntry(final String description, final String amount) {
	    this.description = description;
	    this.amount = amount;
	}

	public String getDescription() {
	    return description;
	}

	public void setDescription(String description) {
	    this.description = description;
	}

	public String getAmount() {
	    return amount;
	}

	public void setAmount(String amount) {
	    this.amount = amount;
	}

    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CreditNoteDocument(final CreditNote creditNote, final IMessageResourceProvider messageResourceProvider,
	    final String basePath, boolean original) {
	this.creditNote = creditNote;
	this.messageResourceProvider = messageResourceProvider;
	this.basePath = basePath;
	this.original = original;

	fillReport();

    }

    @Override
    protected void fillReport() {
	addParameter("documentIdType", this.creditNote.getReceipt().getPerson().getIdDocumentType().getLocalizedName());
	addParameter("documentIdNumber", this.creditNote.getReceipt().getPerson().getDocumentIdNumber());
	addParameter("name", this.creditNote.getReceipt().getPerson().getName());

	addParameter("path", this.basePath);

	addParameter("ownerUnit", this.creditNote.getReceipt().getOwnerUnit().getName());
	addParameter("ownerUnitCostCenter", this.creditNote.getReceipt().getOwnerUnit().getCostCenterCode().toString());

	addParameter("creditNoteNumber", this.creditNote.getNumber() + "/" + this.creditNote.getYear().toString());
	addParameter("receiptNumber", this.creditNote.getReceipt().getNumberWithSeries() + "/"
		+ this.creditNote.getReceipt().getYear().toString());
	addParameter("annulled", this.creditNote.isAnnulled());
	addParameter("creditNoteDate", this.creditNote.getWhenCreated().toString("dd 'de' MMMM 'de' yyyy", Language.getLocale()));
	addParameter("total", this.creditNote.getTotalAmount().toPlainString());

	addParameter("original", this.original);

	addDataSourceElements(buildEntries());

    }

    private List<CreditNoteDocumentEntry> buildEntries() {

	final SortedSet<CreditNoteEntry> sortedEntries = new TreeSet<CreditNoteEntry>(
		CreditNoteEntry.COMPARATOR_BY_WHEN_REGISTERED);
	sortedEntries.addAll(this.creditNote.getCreditNoteEntries());

	final List<CreditNoteDocumentEntry> result = new ArrayList<CreditNoteDocumentEntry>();
	for (final CreditNoteEntry each : sortedEntries) {
	    result.add(new CreditNoteDocumentEntry(each.getAccountingEntry().getDescription().toString(
		    this.messageResourceProvider), each.getAmount().toPlainString()));
	}

	return result;

    }

    @Override
    public String getReportFileName() {
	return "CreditNote-" + new DateTime().toString("yyyyMMddHHmmss");
    }

}
