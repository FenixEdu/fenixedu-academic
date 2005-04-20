/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.projectsManagement.IProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class EditProjectAccess implements IService {

    public EditProjectAccess() {
    }

    public void run(Integer personId, Integer projectCode, Calendar beginDate, Calendar endDate) throws FenixServiceException,
            ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentProjectAccess persistentProjectAccess = sp.getIPersistentProjectAccess();
        IProjectAccess projectAccess = persistentProjectAccess.readByPersonIdAndProjectAndDate(personId, projectCode);
        if (projectAccess == null)
            throw new InvalidArgumentsServiceException();
        projectAccess.setBeginDate(beginDate);
        projectAccess.setEndDate(endDate);
        persistentProjectAccess.simpleLockWrite(projectAccess);
    }
}