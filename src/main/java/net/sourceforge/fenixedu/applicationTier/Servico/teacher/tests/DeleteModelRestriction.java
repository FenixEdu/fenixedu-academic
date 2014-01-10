package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import pt.ist.fenixframework.Atomic;

public class DeleteModelRestriction {
    @Atomic
    public static void run(NewModelRestriction modelRestriction) throws FenixServiceException {
        modelRestriction.delete();
    }
}