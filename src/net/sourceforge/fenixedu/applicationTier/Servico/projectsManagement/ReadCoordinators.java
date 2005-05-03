/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadCoordinators implements IService {

    public ReadCoordinators() {
    }

    public List run(String userView) throws ExcepcaoPersistencia {
        List<InfoRubric> coordinatorsList = new ArrayList<InfoRubric>();

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Integer userNumber = null;

        ITeacher teacher = persistentSuport.getIPersistentTeacher().readTeacherByUsername(userView);
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IPerson person = persistentSuport.getIPessoaPersistente().lerPessoaPorUsername(userView);
            IEmployee employee = persistentSuport.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        if (userNumber != null) {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            Integer thisCoordinator = p.getIPersistentProjectUser().getUserCoordId(userNumber);
            List<Integer> coordinatorsCodes = persistentSuport.getIPersistentProjectAccess().readCoordinatorsCodesByPersonUsernameAndDates(userView);
            if (thisCoordinator != null)
                coordinatorsCodes.add(thisCoordinator);
            for (int coord = 0; coord < coordinatorsCodes.size(); coord++) {
                IRubric rubric = p.getIPersistentProjectUser().readProjectCoordinator((Integer) coordinatorsCodes.get(coord));
                coordinatorsList.add(InfoRubric.newInfoFromDomain(rubric));
            }
        }
        return coordinatorsList;
    }
}