/*
 * Created on 2003/07/29
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadExecutionDegreeByOID {

    @Atomic
    public static InfoExecutionDegree run(String oid) {
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(oid);
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}