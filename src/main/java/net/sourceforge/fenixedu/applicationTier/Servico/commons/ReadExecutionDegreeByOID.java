/*
 * Created on 2003/07/29
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadExecutionDegreeByOID {

    @Service
    public static InfoExecutionDegree run(String oid) {
        final ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(oid);
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}