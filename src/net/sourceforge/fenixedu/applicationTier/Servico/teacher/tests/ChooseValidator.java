package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;

public class ChooseValidator extends FenixService {
    public void run(NewAtomicQuestion atomicQuestion, Predicate validator) throws FenixServiceException {
	atomicQuestion.setValidator(validator);
    }
}
