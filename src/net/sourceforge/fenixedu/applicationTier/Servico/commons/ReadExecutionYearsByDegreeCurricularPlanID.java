/*
 * Created on 9/Fev/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class ReadExecutionYearsByDegreeCurricularPlanID extends Service {

    public List run(Integer degreeCurricularPlanID) {
	DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);

	List<ExecutionYear> executionYears = (List<ExecutionYear>) CollectionUtils.collect(degreeCurricularPlan
		.getExecutionDegrees(), new Transformer() {

	    public Object transform(Object arg0) {
		ExecutionDegree executionDegree = (ExecutionDegree) arg0;
		return InfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear());
	    }

	});

	return executionYears;
    }
}