package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import pt.ist.fenixWebFramework.services.Service;

public class CreateChoice extends FenixService {
    @Service
    public static NewChoice run(NewMultipleChoiceQuestion multipleChoiceQuestion) throws FenixServiceException {
        return new NewChoice(multipleChoiceQuestion);
    }
}