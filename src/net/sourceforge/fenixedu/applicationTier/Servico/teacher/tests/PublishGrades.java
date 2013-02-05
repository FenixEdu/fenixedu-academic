package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import pt.ist.fenixWebFramework.services.Service;

public class PublishGrades extends FenixService {
    @Service
    public static void run(NewTestGroup testGroup) throws FenixServiceException {
        testGroup.publishGrades();
    }
}