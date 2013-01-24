package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.accounting.Exemption;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteExemption {

    @Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
    @Service
    public static void run(final Exemption exemption) {
	exemption.delete();
    }

}