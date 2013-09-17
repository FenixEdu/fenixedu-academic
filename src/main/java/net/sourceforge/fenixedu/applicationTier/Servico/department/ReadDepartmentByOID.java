/*
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.Department;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Fialho & Rita Ferreira
 */
public class ReadDepartmentByOID {

    @Atomic
    public static InfoDepartment run(String oid) throws FenixServiceException {
        Department department = FenixFramework.getDomainObject(oid);
        return InfoDepartment.newInfoFromDomain(department);
    }
}