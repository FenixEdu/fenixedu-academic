package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import net.sourceforge.fenixedu.domain.tests.NewQuestionType;
import pt.ist.fenixframework.Atomic;

public class CreateAtomicQuestion {
    @Atomic
    public static NewQuestion run(NewQuestionGroup parentQuestionGroup, NewQuestionType questionType)
            throws FenixServiceException {
        return questionType.newInstance(parentQuestionGroup);
    }
}