package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.applicationTier.Service;

public class EditProjectAccess extends Service {

    public void run(String username, String costCenter, Integer personId, Integer projectCode,
            Calendar beginDate, Calendar endDate, String userNumber) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentProjectAccess persistentProjectAccess = sp.getIPersistentProjectAccess();
        final ProjectAccess projectAccess = persistentProjectAccess.readByPersonIdAndProjectAndDate(
                personId, projectCode);
        if (projectAccess == null) {
            throw new InvalidArgumentsServiceException();
        }
        
        projectAccess.setBeginDate(beginDate);
        projectAccess.setEndDate(endDate);
    }

}
