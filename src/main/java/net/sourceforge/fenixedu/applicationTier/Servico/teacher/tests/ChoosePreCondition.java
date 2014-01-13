package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;
import pt.ist.fenixframework.Atomic;

public class ChoosePreCondition {
    @Atomic
    public static void run(NewQuestion question, Predicate preCondition) throws FenixServiceException {
        question.setPreCondition(preCondition);
    }
}