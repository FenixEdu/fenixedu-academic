package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;
import pt.ist.fenixWebFramework.services.Service;

public class ChooseCorrector {
    @Service
    public static void run(NewAtomicQuestion atomicQuestion, Predicate predicate, Integer percentage)
            throws FenixServiceException {
        new NewCorrector(atomicQuestion, predicate, percentage);
    }
}