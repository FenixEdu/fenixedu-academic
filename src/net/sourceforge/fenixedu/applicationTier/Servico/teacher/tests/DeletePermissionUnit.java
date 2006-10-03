package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewPermissionUnit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeletePermissionUnit extends Service {
    public void run(NewPermissionUnit permissionUnit) throws FenixServiceException, ExcepcaoPersistencia {
        permissionUnit.delete();
    }
}
