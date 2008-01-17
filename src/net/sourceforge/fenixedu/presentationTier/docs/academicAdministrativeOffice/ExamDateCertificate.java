package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;

public class ExamDateCertificate extends AdministrativeOfficeDocument {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected ExamDateCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void fillReport() {
	super.fillReport();

	this.dataSource.addAll(getExamDateEntries());

	parameters.put("name", getDocumentRequest().getRegistration().getPerson().getName());
    }

    private List<ExamDateEntry> getExamDateEntries() {
	final List<ExamDateEntry> result = new ArrayList<ExamDateEntry>();

	for (int i = 0; i < 50; i++) {
	    result.add(new ExamDateEntry("cool" + i, "data xpto" + i));
	}

	return result;
    }

    @Override
    protected boolean hasPayment() {
	return false;
    }

}
