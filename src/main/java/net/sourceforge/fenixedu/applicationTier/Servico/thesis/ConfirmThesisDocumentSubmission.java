package net.sourceforge.fenixedu.applicationTier.Servico.thesis;


import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class ConfirmThesisDocumentSubmission {

    @Atomic
    public static void run(Thesis thesis) {
        thesis.setConfirmmedDocuments(new DateTime());
    }

}