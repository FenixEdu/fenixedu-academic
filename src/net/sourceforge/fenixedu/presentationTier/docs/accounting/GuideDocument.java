package net.sourceforge.fenixedu.presentationTier.docs.accounting;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.resources.IMessageResourceProvider;

public class GuideDocument extends FenixReport {

    private final PaymentsManagementDTO paymentsManagementDTO;

    private final IMessageResourceProvider messageResourceProvider;

    private final Unit unit;

    public static class GuideDocumentEntry {
	private String description;

	private String amountToPay;

	public GuideDocumentEntry(final String description, final String amountToPay) {
	    this.description = description;
	    this.amountToPay = amountToPay;
	}

	public String getDescription() {
	    return description;
	}

	public void setDescription(String description) {
	    this.description = description;
	}

	public String getAmountToPay() {
	    return amountToPay;
	}

	public void setAmountToPay(String amountToPay) {
	    this.amountToPay = amountToPay;
	}

    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public GuideDocument(final PaymentsManagementDTO paymentsManagementDTO, final Unit unit,
	    final IMessageResourceProvider messageResourceProvider) {
	this.paymentsManagementDTO = paymentsManagementDTO;
	this.unit = unit;
	this.messageResourceProvider = messageResourceProvider;
	fillReport();
    }

    @Override
    protected void fillReport() {

	addParameter("total", this.paymentsManagementDTO.getTotalAmountToPay().toPlainString());
	addParameter("date", new LocalDate().toString(DD_MMMM_YYYY, getLocale()));
	addParameter("unit", unit.getName());
	addParameter("unitPhone", unit.getDefaultPhoneNumber());
	addParameter("costCenter", unit.getCostCenterCode().toString());
	addParameter("documentIdType", this.paymentsManagementDTO.getPerson().getIdDocumentType().getLocalizedName());
	addParameter("documentIdNumber", this.paymentsManagementDTO.getPerson().getDocumentIdNumber());
	addParameter("name", this.paymentsManagementDTO.getPerson().getName());
	addParameter("studentNumber", this.paymentsManagementDTO.getPerson().hasStudent() ? this.paymentsManagementDTO
		.getPerson().getStudent().getNumber().toString() : null);

	addDataSourceElements(buildEntries());
    }

    private List<GuideDocumentEntry> buildEntries() {

	final List<GuideDocumentEntry> result = new ArrayList<GuideDocumentEntry>();

	for (final EntryDTO entryDTO : this.paymentsManagementDTO.getSelectedEntries()) {
	    result.add(new GuideDocumentEntry(entryDTO.getDescription().toString(this.messageResourceProvider), entryDTO
		    .getAmountToPay().toPlainString()));
	}

	return result;

    }

    @Override
    public String getReportFileName() {
	return "Guide-" + new DateTime().toString(YYYYMMDDHHMMSS, getLocale());
    }

}
