package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;

public class ChoosePreCondition extends Service {
    public void run(NewQuestion question, Predicate preCondition) throws FenixServiceException {
	question.setPreCondition(preCondition);
    }
}
