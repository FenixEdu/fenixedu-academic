package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import pt.ist.fenixWebFramework.services.Service;

public class GiveUpQuestion extends FenixService {
    @Service
    public static void run(NewAtomicQuestion atomicQuestion) throws FenixServiceException {
        atomicQuestion.giveUpQuestion();
    }
}