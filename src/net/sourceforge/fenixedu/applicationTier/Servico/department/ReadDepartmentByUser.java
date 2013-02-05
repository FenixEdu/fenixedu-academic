/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author jpvl
 */
public class ReadDepartmentByUser extends FenixService {

    @Service
    public static InfoDepartment run(String username) throws FenixServiceException {
        InfoDepartment infoDepartment = null;

        final Person person = Person.readPersonByUsername(username);
        final List<Department> departmentList = person.getManageableDepartmentCredits();
        if (!departmentList.isEmpty()) {
            infoDepartment = InfoDepartment.newInfoFromDomain(departmentList.get(0));
        }
        return infoDepartment;
    }
}