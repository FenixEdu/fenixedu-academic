/*
 * Created on Feb 20, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 * @return List containing all InfoExecutionDegrees, corresponding to Degree
 *         Curricular Plan
 * 
 */
public class ReadExecutionDegreesByDegreeCurricularPlanID extends FenixService {

	@Service
	public static List<InfoExecutionDegree> run(Integer degreeCurricularPlanID) {
		DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);

		List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>();

		for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
			result.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));
		}

		return result;
	}

}