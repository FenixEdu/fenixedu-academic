/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.department;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDepartment;
import DataBeans.util.Cloner;
import Dominio.IDepartment;
import Dominio.IPerson;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadDepartmentByUser implements IService {
    public ReadDepartmentByUser() {
    }

    public InfoDepartment run(String username) throws FenixServiceException {
        InfoDepartment infoDepartment = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            IPerson person = personDAO.lerPessoaPorUsername(username);
            List departmentList = person.getManageableDepartmentCredits();
            if (departmentList != null && !departmentList.isEmpty()) {
                infoDepartment = Cloner.copyIDepartment2InfoDepartment((IDepartment) departmentList
                        .get(0));
            }
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException("Problems on database!", e);
        }
        return infoDepartment;
    }
}