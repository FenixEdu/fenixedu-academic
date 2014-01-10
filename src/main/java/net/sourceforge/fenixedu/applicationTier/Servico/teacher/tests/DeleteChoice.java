package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import pt.ist.fenixframework.Atomic;

public class DeleteChoice {
    @Atomic
    public static void run(NewChoice choice) throws FenixServiceException {
        choice.delete();
    }
}