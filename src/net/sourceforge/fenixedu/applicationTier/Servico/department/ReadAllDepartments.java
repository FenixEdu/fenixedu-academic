/*
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a>
 */
public class ReadAllDepartments extends Service {

    public List run() throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentDepartment departmentDAO = persistentSupport.getIDepartamentoPersistente();
        List departments = departmentDAO.readAll();
        Iterator iter = departments.iterator();
        List infoDepartments = new ArrayList();
        while (iter.hasNext()) {
            Department department = (Department) iter.next();
            InfoDepartment infoDepartment = InfoDepartment.newInfoFromDomain(department);
            infoDepartments.add(infoDepartment);
        }
        return infoDepartments;
    }
}