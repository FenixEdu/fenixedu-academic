package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewPermissionUnit;
import pt.ist.fenixframework.Atomic;

public class DeletePermissionUnit {
    @Atomic
    public static void run(NewPermissionUnit permissionUnit) throws FenixServiceException {
        permissionUnit.delete();
    }
}