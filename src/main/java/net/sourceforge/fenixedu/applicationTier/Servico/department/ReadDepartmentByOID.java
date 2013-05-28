/*
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.Department;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author João Fialho & Rita Ferreira
 */
public class ReadDepartmentByOID {

    @Service
    public static InfoDepartment run(Integer oid) throws FenixServiceException {
        Department department = AbstractDomainObject.fromExternalId(oid);
        return InfoDepartment.newInfoFromDomain(department);
    }
}