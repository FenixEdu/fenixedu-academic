package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import pt.ist.fenixframework.Atomic;

public class DisassociateParent {
    @Atomic
    public static void run(NewQuestionGroup parent, NewQuestion child) throws FenixServiceException {
        parent.disassociate(child);
    }
}