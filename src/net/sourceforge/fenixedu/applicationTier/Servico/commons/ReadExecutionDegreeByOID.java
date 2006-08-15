/*
 * Created on 2003/07/29
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadExecutionDegreeByOID extends Service {

	public InfoExecutionDegree run(Integer oid) throws ExcepcaoPersistencia {
		final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(oid);
		return InfoExecutionDegree.newInfoFromDomain(executionDegree);
	}

}