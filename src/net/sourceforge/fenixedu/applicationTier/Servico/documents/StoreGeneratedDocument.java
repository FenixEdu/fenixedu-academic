package net.sourceforge.fenixedu.applicationTier.Servico.documents;

import java.io.InputStream;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.documents.CreditNoteGeneratedDocument;
import net.sourceforge.fenixedu.domain.documents.DocumentRequestGeneratedDocument;
import net.sourceforge.fenixedu.domain.documents.ReceiptGeneratedDocument;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;

public class StoreGeneratedDocument extends FenixService {
    public static final String CONFIG_DSPACE_DOCUMENT_STORE = "dspace.generated.document.store";

    @Service
    public static void run(String filename, InputStream stream, Object source) throws FenixServiceException {
	if (source instanceof CreditNote) {
	    CreditNote creditNote = (CreditNote) source;
	    new CreditNoteGeneratedDocument(creditNote, creditNote.getReceipt().getPerson(), AccessControl.getPerson(), filename,
		    stream);
	} else if (source instanceof Receipt) {
	    Receipt receipt = (Receipt) source;
	    new ReceiptGeneratedDocument(receipt, receipt.getPerson(), AccessControl.getPerson(), filename, stream);
	} else if (source instanceof DocumentRequest) {
	    DocumentRequest request = (DocumentRequest) source;
	    new DocumentRequestGeneratedDocument(request, request.getStudent().getPerson(), AccessControl.getPerson(), filename,
		    stream);
	} else
	    throw new FenixServiceException("Document Storage: source not recognised: " + source.getClass().getSimpleName());
    }
}