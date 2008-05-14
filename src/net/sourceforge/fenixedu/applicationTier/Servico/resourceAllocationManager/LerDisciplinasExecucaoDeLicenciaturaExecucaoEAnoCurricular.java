package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * Serviço LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular extends Service {

    public List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod, Integer curricularYearID)
	    throws ExcepcaoPersistencia {

	List listInfoDE = new ArrayList();

	CurricularYear curricularYear = rootDomainObject.readCurricularYearByOID(curricularYearID);
	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(infoExecutionPeriod.getIdInternal());
	DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(infoExecutionDegree
		.getInfoDegreeCurricularPlan().getIdInternal());

	if (executionSemester != null) {
	    List<ExecutionCourse> listDCDE = executionSemester
		    .getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(degreeCurricularPlan,
			    curricularYear, "%");

	    Iterator iterator = listDCDE.iterator();
	    listInfoDE = new ArrayList();
	    while (iterator.hasNext()) {
		ExecutionCourse elem = (ExecutionCourse) iterator.next();

		listInfoDE.add(InfoExecutionCourse.newInfoFromDomain(elem));

	    }
	}
	return listInfoDE;
    }

}