package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;
import pt.ist.fenixWebFramework.services.Service;

public class ChooseValidator {
    @Service
    public static void run(NewAtomicQuestion atomicQuestion, Predicate validator) throws FenixServiceException {
        atomicQuestion.setValidator(validator);
    }
}