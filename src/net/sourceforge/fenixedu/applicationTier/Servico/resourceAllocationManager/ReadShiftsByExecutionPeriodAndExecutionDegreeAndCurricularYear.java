/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 * 
 * Created on 2003/08/09
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear extends Service {

    public List<InfoShift> run(InfoExecutionPeriod infoExecutionPeriod, InfoExecutionDegree infoExecutionDegree,
	    InfoCurricularYear infoCurricularYear) {

	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(infoExecutionPeriod
		.getIdInternal());
	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
	final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
	final CurricularYear curricularYear = rootDomainObject.readCurricularYearByOID(infoCurricularYear.getIdInternal());

	final List<InfoShift> infoShifts = new ArrayList<InfoShift>();
	final List<ExecutionCourse> executionCourses = executionSemester
		.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(degreeCurricularPlan,
			curricularYear, "%");
	for (final ExecutionCourse executionCourse : executionCourses) {
	    for (final Shift shift : executionCourse.getAssociatedShifts()) {
		final InfoShift infoShift = new InfoShift(shift);
		infoShifts.add(infoShift);
	    }
	}

	return infoShifts;

    }

}