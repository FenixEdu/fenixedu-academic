package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteModelRestriction extends FenixService {
    @Service
    public static void run(NewModelRestriction modelRestriction) throws FenixServiceException {
        modelRestriction.delete();
    }
}