/*
 * Created on Jan 11, 2005
 */

package ServidorAplicacao.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import pt.utl.ist.berserk.storage.exceptions.StorageException;
import DataBeans.projectsManagement.InfoProject;
import Dominio.IEmployee;
import Dominio.IPessoa;
import Dominio.ITeacher;
import Dominio.projectsManagement.IProject;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteOracle.Oracle.PersistentSuportOracle;

/**
 * @author Susana Fernandes
 */
public class ReadUserProjects implements IService {

    public ReadUserProjects() {
    }

    public List run(IUserView userView) throws FenixServiceException, ExcepcaoPersistencia, StorageException {
        List infoProjectList = new ArrayList();

        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

        Integer userNumber = null;

        ITeacher teacher = (ITeacher) persistentSuport.getIPersistentTeacher().readTeacherByUsername(userView.getUtilizador());
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IPessoa person = persistentSuport.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = (IEmployee) persistentSuport.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        if (userNumber != null) {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            List projectList = p.getIPersistentProject().getAllProjectsByUserLogin(userNumber.toString());
            for (int i = 0; i < projectList.size(); i++)
                infoProjectList.add(InfoProject.newInfoFromDomain((IProject) projectList.get(i)));
        }
        return infoProjectList;
    }
}