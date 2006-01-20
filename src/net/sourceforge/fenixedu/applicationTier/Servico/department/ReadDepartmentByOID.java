/*
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;

/**
 * @author João Fialho & Rita Ferreira
 */
public class ReadDepartmentByOID extends Service {

    public InfoDepartment run(Integer oid) throws FenixServiceException, ExcepcaoPersistencia {
        InfoDepartment infoDepartment = null;
        IPersistentDepartment departmentDAO = persistentSupport.getIDepartamentoPersistente();

        Department department = (Department) departmentDAO.readByOID(Department.class, oid);
        infoDepartment = InfoDepartment.newInfoFromDomain(department);

        return infoDepartment;

    }
}