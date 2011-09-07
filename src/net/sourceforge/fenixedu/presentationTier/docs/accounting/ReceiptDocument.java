package net.sourceforge.fenixedu.presentationTier.docs.accounting;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.resources.IMessageResourceProvider;

public class ReceiptDocument extends FenixReport {

    private final Receipt receipt;

    private final IMessageResourceProvider messageResourceProvider;

    private final boolean original;

    public static class ReceiptDocumentEntry {
	private String description;

	private String amount;

	public ReceiptDocumentEntry(final String description, final String amount) {
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

    public ReceiptDocument(final Receipt receipt, final IMessageResourceProvider messageResourceProvider, boolean original) {
	this.receipt = receipt;
	this.messageResourceProvider = messageResourceProvider;
	this.original = original;

	fillReport();

    }

    @Override
    protected void fillReport() {

	addParameter("ownerUnit", this.receipt.getOwnerUnit().getName());
	final Integer costCenter = this.receipt.getOwnerUnit().getCostCenterCode();
	addParameter("ownerUnitCostCenter", costCenter == null ? "" : costCenter.toString());
	addParameter("creatorUnit", this.receipt.getCreatorUnit().getName());
	addParameter("emmittedByOtherUnit", this.receipt.getOwnerUnit() != this.receipt.getCreatorUnit());
	addParameter("documentIdType", this.receipt.getPerson().getIdDocumentType().getLocalizedName());
	addParameter("documentIdNumber", this.receipt.getPerson().getDocumentIdNumber());
	addParameter("name", this.receipt.getPerson().getName());

	addParameter("number", this.receipt.getNumberWithSeries());
	addParameter("year", this.receipt.getYear().toString());
	addParameter("secondPrintVersion", this.receipt.isSecondPrintVersion());
	addParameter("annulled", this.receipt.isAnnulled());
	addParameter("receiptDate", this.receipt.getReceiptDate().toString(DD_MMMM_YYYY, getLocale()));
	addParameter("total", this.receipt.getTotalAmount().toPlainString());

	addParameter("original", this.original);
	if (this.receipt.hasContributorParty()) {
	    addParameter("contributorName", this.receipt.getContributorParty().getName());
	    addParameter("contributorSocialSecurityNumber", this.receipt.getContributorParty().getSocialSecurityNumber());
	    addParameter("contributorAddress", this.receipt.getContributorParty().getAddress());
	    addParameter("contributorArea", !StringUtils.isEmpty(this.receipt.getContributorParty().getAreaCode()) ? this.receipt
		    .getContributorParty().getAreaCode()
		    + SINGLE_SPACE + this.receipt.getContributorParty().getAreaOfAreaCode() : null);
	} else {
	    addParameter("contributorName", this.receipt.getContributorName());
	    addParameter("contributorSocialSecurityNumber", Receipt.GENERIC_CONTRIBUTOR_PARTY_NUMBER);
	}

	addParameter("studentNumber", this.receipt.getPerson().hasStudent() ? this.receipt.getPerson().getStudent().getNumber()
		.toString() : null);

	addDataSourceElements(buildEntries());

    }

    private List<ReceiptDocumentEntry> buildEntries() {

	final List<ReceiptDocumentEntry> result = new ArrayList<ReceiptDocumentEntry>();

	final SortedSet<Entry> sortedEntries = new TreeSet<Entry>(Entry.COMPARATOR_BY_MOST_RECENT_WHEN_REGISTERED);
	sortedEntries.addAll(receipt.getEntries());

	for (final Entry entry : sortedEntries) {
	    result.add(new ReceiptDocumentEntry(entry.getDescription().toString(this.messageResourceProvider), entry
		    .getOriginalAmount().toPlainString()));
	}

	return result;

    }

    @Override
    public String getReportFileName() {
	return "Receipt-" + new DateTime().toString(YYYYMMDDHHMMSS, getLocale());
    }

}
