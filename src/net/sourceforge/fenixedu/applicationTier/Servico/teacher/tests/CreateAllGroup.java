package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewAllGroup;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;

public class CreateAllGroup extends FenixService {
    public NewAllGroup run(NewQuestionGroup parentQuestionGroup) throws FenixServiceException {
	return new NewAllGroup(parentQuestionGroup);
    }
}
