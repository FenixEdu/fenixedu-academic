package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;
import pt.ist.fenixWebFramework.services.Service;

public class ChoosePreCondition {
    @Service
    public static void run(NewQuestion question, Predicate preCondition) throws FenixServiceException {
        question.setPreCondition(preCondition);
    }
}