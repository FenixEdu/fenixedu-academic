/*
 * Created on 30/Oct/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonRole;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.PersonRole;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPersonRole;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota 30/Oct/2003
 *  
 */
public class RemoveCoordinators implements IService {

    public Boolean run(Integer executionDegreeId, List coordinatorsIds) throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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