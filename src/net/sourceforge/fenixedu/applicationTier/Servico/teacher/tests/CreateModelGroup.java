package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;

public class CreateModelGroup extends FenixService {
    public NewModelGroup run(NewModelGroup parentGroup, String name) throws FenixServiceException {
	return new NewModelGroup(parentGroup, name);
    }
}
