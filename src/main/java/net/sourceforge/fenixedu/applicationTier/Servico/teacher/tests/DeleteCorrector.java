package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import pt.ist.fenixframework.Atomic;

public class DeleteCorrector {
    @Atomic
    public static void run(NewCorrector corrector) throws FenixServiceException {
        corrector.delete();
    }
}