/*
 * Created on 2003/07/29
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadExecutionDegreeByOID extends FenixService {

    @Service
    public static InfoExecutionDegree run(Integer oid) {
        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(oid);
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}