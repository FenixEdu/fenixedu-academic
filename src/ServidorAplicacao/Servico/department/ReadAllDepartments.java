/*
 * 
 *  
 */
package ServidorAplicacao.Servico.department;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDepartment;
import DataBeans.util.Cloner;
import Dominio.IDepartment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 */
public class ReadAllDepartments implements IService {
    public ReadAllDepartments() {
    }

    public List run() throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();
            List departments = departmentDAO.readAllDepartments();
            Iterator iter = departments.iterator();
            List infoDepartments = new ArrayList();
            while (iter.hasNext()) {
                IDepartment department = (IDepartment) iter.next();
                InfoDepartment infoDepartment = Cloner.copyIDepartment2InfoDepartment(department);
                infoDepartments.add(infoDepartment);
            }
            return infoDepartments;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}