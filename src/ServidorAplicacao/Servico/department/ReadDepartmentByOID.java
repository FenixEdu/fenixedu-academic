/*
 * 
 *  
 */
package ServidorAplicacao.Servico.department;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDepartment;
import DataBeans.util.Cloner;
import Dominio.Department;
import Dominio.IDepartment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Fialho & Rita Ferreira
 */
public class ReadDepartmentByOID implements IService {
    public ReadDepartmentByOID() {
    }

    public InfoDepartment run(Integer oid) throws FenixServiceException {

    	InfoDepartment infoDepartment = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();


            IDepartment department = (IDepartment)departmentDAO.readByOID(Department.class, oid);
            infoDepartment = Cloner.copyIDepartment2InfoDepartment(department);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }
        return infoDepartment;

    }
}