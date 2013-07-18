package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import net.sourceforge.fenixedu.domain.tests.NewQuestionType;
import pt.ist.fenixWebFramework.services.Service;

public class CreateAtomicQuestion {
    @Service
    public static NewQuestion run(NewQuestionGroup parentQuestionGroup, NewQuestionType questionType)
            throws FenixServiceException {
        return questionType.newInstance(parentQuestionGroup);
    }
}