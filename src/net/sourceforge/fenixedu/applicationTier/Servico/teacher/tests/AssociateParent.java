package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;

public class AssociateParent extends FenixService {
    public void run(NewQuestionGroup parent, NewQuestion child) throws FenixServiceException {
	child.setParentQuestionGroup(parent);
    }
}
