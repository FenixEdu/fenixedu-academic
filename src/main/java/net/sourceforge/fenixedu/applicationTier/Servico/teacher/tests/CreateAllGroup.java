package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewAllGroup;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import pt.ist.fenixWebFramework.services.Service;

public class CreateAllGroup {
    @Service
    public static NewAllGroup run(NewQuestionGroup parentQuestionGroup) throws FenixServiceException {
        return new NewAllGroup(parentQuestionGroup);
    }
}