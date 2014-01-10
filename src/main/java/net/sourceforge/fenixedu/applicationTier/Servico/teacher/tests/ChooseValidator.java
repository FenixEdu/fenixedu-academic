package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;
import pt.ist.fenixframework.Atomic;

public class ChooseValidator {
    @Atomic
    public static void run(NewAtomicQuestion atomicQuestion, Predicate validator) throws FenixServiceException {
        atomicQuestion.setValidator(validator);
    }
}