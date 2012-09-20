/*
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.Department;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 */
public class ReadDepartmentByOID extends FenixService {

    @Service
    public static InfoDepartment run(Integer oid) throws FenixServiceException {
	Department department = rootDomainObject.readDepartmentByOID(oid);
	return InfoDepartment.newInfoFromDomain(department);
    }
}