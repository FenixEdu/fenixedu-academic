/*
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.Atomic;

public class ReadAllDepartments {

    @Atomic
    public static List<InfoDepartment> run() throws FenixServiceException {
        final List<InfoDepartment> result = new ArrayList<InfoDepartment>();
        for (final Department department : RootDomainObject.getInstance().getDepartments()) {
            result.add(InfoDepartment.newInfoFromDomain(department));
        }
        return result;
    }
}