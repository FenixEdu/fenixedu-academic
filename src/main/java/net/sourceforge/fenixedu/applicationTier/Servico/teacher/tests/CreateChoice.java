package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import pt.ist.fenixframework.Atomic;

public class CreateChoice {
    @Atomic
    public static NewChoice run(NewMultipleChoiceQuestion multipleChoiceQuestion) throws FenixServiceException {
        return new NewChoice(multipleChoiceQuestion);
    }
}