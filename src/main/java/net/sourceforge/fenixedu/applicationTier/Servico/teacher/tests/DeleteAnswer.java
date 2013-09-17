package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import pt.ist.fenixframework.Atomic;

public class DeleteAnswer {
    @Atomic
    public static void run(NewAtomicQuestion atomicQuestion) throws FenixServiceException {
        atomicQuestion.deleteAnswer();
    }
}