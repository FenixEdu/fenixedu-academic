/*
 * Created on 2003/07/29
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadExecutionDegreeByOID {

    @Service
    public static InfoExecutionDegree run(Integer oid) {
        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(oid);
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}