/*
 * Created on Jan 11, 2005
 */

package ServidorAplicacao.Servico.projectsManagement;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.projectsManagement.IProjectAccess;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.projectsManagement.IPersistentProjectAccess;

/**
 * @author Susana Fernandes
 */
public class EditProjectAccess implements IService {

    public EditProjectAccess() {
    }

    public void run(IUserView userView, Integer personId, Integer projectCode, Calendar beginDate, Calendar endDate) throws FenixServiceException,
            ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentProjectAccess persistentProjectAccess = sp.getIPersistentProjectAccess();
        IProjectAccess projectAccess = persistentProjectAccess.readByPersonIdAndProjectAndDate(personId, projectCode);
        if (projectAccess == null)
            throw new InvalidArgumentsServiceException();
        projectAccess.setBeginDate(beginDate);
        projectAccess.setEndDate(endDate);
        persistentProjectAccess.simpleLockWrite(projectAccess);
    }
}