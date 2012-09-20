/*
 * Created on 3/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 3/Dez/2003
 * 
 */
public class ReadExecutionCoursesByDegreeAndExecutionPeriodId extends FenixService {

    @Service
    public static List run(Integer degreeId, AcademicInterval academicInterval) throws FenixServiceException {
	final List infoExecutionCourses = new ArrayList();

	if (academicInterval == null) {
	    throw new InvalidArgumentsServiceException();
	}

	final Degree degree = rootDomainObject.readDegreeByOID(degreeId);
	if (degree == null) {
	    throw new InvalidArgumentsServiceException();
	}
	final ExecutionDegree executionDegree = findExecutionDegree(academicInterval, degree);
	if (executionDegree != null) {
	    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

	    for (final ExecutionCourse executionCourse : ExecutionCourse.filterByAcademicInterval(academicInterval)) {
		if (satisfiesCriteria(executionCourse, degreeCurricularPlan)) {
		    infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
		}
	    }
	}

	return infoExecutionCourses;
    }

    private static ExecutionDegree findExecutionDegree(final AcademicInterval academicInterval, final Degree degree) {
	List<ExecutionDegree> all = ExecutionDegree.filterByAcademicInterval(academicInterval);
	for (ExecutionDegree executionDegree : all) {
	    if (executionDegree.getDegree().equals(degree))
		return executionDegree;
	}
	return null;
    }

    private static boolean satisfiesCriteria(final ExecutionCourse executionCourse,
	    final DegreeCurricularPlan degreeCurricularPlan) {
	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
	    if (curricularCourse.getDegreeCurricularPlan() == degreeCurricularPlan) {
		return true;
	    }
	}
	return false;
    }

}