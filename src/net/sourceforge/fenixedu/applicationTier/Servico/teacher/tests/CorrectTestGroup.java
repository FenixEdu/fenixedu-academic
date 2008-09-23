package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;

public class CorrectTestGroup extends FenixService {
    public void run(NewTestGroup testGroup) throws FenixServiceException {
	testGroup.correct();
    }
}
