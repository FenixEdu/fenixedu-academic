package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;

public class CreateChoice extends Service {
    public NewChoice run(NewMultipleChoiceQuestion multipleChoiceQuestion) throws FenixServiceException {
	return new NewChoice(multipleChoiceQuestion);
    }
}
