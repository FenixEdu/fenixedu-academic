package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import pt.ist.fenixframework.Atomic;

public class DeleteGrade {
    @Atomic
    public static void run(NewQuestion question) throws FenixServiceException {
        question.setGrade(null);
    }
}