package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;

public class DisassociateParent extends Service {
    public void run(NewQuestionGroup parent, NewQuestion child) throws FenixServiceException {
	parent.disassociate(child);
    }
}
