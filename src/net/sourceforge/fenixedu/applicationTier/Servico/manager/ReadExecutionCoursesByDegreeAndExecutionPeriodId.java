/*
 * Created on 3/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 3/Dez/2003
 * 
 */
public class ReadExecutionCoursesByDegreeAndExecutionPeriodId extends Service {

    public List run(Integer degreeId, Integer executionPeriodId) throws FenixServiceException{
	final List infoExecutionCourses = new ArrayList();

	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);
	if (executionSemester == null) {
	    throw new InvalidArgumentsServiceException();
	}

	final Degree degree = rootDomainObject.readDegreeByOID(degreeId);
	if (degree == null) {
	    throw new InvalidArgumentsServiceException();
	}
	final ExecutionDegree executionDegree = findExecutionDegree(executionSemester, degree);
	if (executionDegree != null) {
	    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

	    for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCourses()) {
		if (satisfiesCriteria(executionCourse, degreeCurricularPlan)) {
		    infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
		}
	    }
	}

	return infoExecutionCourses;
    }

    private ExecutionDegree findExecutionDegree(final ExecutionSemester executionSemester, final Degree degree) {
	final ExecutionYear executionYear = executionSemester.getExecutionYear();
	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
	    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
	    if (degreeCurricularPlan.getDegree() == degree) {
		return executionDegree;
	    }
	}
	return null;
    }

    private boolean satisfiesCriteria(final ExecutionCourse executionCourse, final DegreeCurricularPlan degreeCurricularPlan) {
	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
	    if (curricularCourse.getDegreeCurricularPlan() == degreeCurricularPlan) {
		return true;
	    }
	}
	return false;
    }

}