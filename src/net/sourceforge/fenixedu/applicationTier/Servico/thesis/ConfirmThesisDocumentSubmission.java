package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.joda.time.DateTime;

public class ConfirmThesisDocumentSubmission extends Service {

    public void run(Thesis thesis) {
	thesis.setConfirmmedDocuments(new DateTime());
    }

}
