/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.projectsManagement.IProject;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadUserProjects implements IService {

    public ReadUserProjects() {
    }

    public List run(IUserView userView, Boolean all) throws FenixServiceException, ExcepcaoPersistencia {
        List infoProjectList = new ArrayList();

        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

        Integer userNumber = null;

        ITeacher teacher = (ITeacher) persistentSuport.getIPersistentTeacher().readTeacherByUsername(userView.getUtilizador());
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IPerson person = persistentSuport.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = (IEmployee) persistentSuport.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        if (userNumber != null) {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            List projectList = p.getIPersistentProject().readByUserLogin(userNumber.toString());
            if (all.booleanValue())
                projectList.addAll(p.getIPersistentProject().readByProjectsCodes(
                        persistentSuport.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndDate(userView.getUtilizador())));
            for (int i = 0; i < projectList.size(); i++)
                infoProjectList.add(InfoProject.newInfoFromDomain((IProject) projectList.get(i)));
        }
        return infoProjectList;
    }    
}