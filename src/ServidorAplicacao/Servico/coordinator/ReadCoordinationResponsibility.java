/*
 * Created on 17/Set/2003
 *
 */
package ServidorAplicacao.Servico.coordinator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ICoordinator;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota 17/Set/2003
 *  
 */
public class ReadCoordinationResponsibility implements IService {

    public Boolean run(Integer executionDegreeId, IUserView userView) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(userView.getUtilizador());
            if (teacher == null) {
                throw new InvalidArgumentsServiceException("invalid teacher!");
            }
            ICoordinator coordinator = persistentCoordinator
                    .readCoordinatorByTeacherAndExecutionDegreeId(teacher, executionDegreeId);
            if (coordinator == null || !coordinator.getResponsible().booleanValue()) {
                return new Boolean(false);
            }
            return new Boolean(true);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}