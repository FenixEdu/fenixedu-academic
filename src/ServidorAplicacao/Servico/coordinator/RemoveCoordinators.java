/*
 * Created on 30/Oct/2003
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Coordinator;
import Dominio.ICoordinator;
import Dominio.IPersonRole;
import Dominio.IPerson;
import Dominio.ITeacher;
import Dominio.PersonRole;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentPersonRole;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota 30/Oct/2003
 *  
 */
public class RemoveCoordinators implements IService {

    public Boolean run(Integer executionDegreeId, List coordinatorsIds) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
            IPersistentPersonRole persistentPersonRole = sp.getIPersistentPersonRole();

            Iterator iter = coordinatorsIds.iterator();
            while (iter.hasNext()) {
                ICoordinator coordinator = (ICoordinator) persistentCoordinator.readByOID(
                        Coordinator.class, (Integer) iter.next(), true);

                if (coordinator != null) {
                    IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
                    ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, coordinator
                            .getTeacher().getIdInternal());
                    if (teacher == null) {
                        throw new NonExistingServiceException();
                    }

                    //verify if the teacher already was coordinator
                    //it he was that don't delete role COORDINATOR
                    List executionDegreesTeacherList = persistentCoordinator
                            .readExecutionDegreesByTeacher(teacher);
                    if (executionDegreesTeacherList == null || executionDegreesTeacherList.size() <= 0) {
                        IPerson person = coordinator.getTeacher().getPerson();
                        IPersonRole personRole = persistentPersonRole.readByPersonAndRole(person, sp
                                .getIPersistentRole().readByRoleType(RoleType.COORDINATOR));
                        if (personRole != null) {
                            persistentPersonRole.deleteByOID(PersonRole.class, personRole
                                    .getIdInternal());
                        }
                    }
                    persistentCoordinator.deleteByOID(Coordinator.class, coordinator.getIdInternal());
                }
            }

            return new Boolean(true);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}