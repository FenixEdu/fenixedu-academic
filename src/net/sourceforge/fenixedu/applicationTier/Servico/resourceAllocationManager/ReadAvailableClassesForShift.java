/*
 * Created on 30/Jun/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;

/**
 * @author Jo�o Mota
 * 
 *         30/Jun/2003 fenix-branch ServidorAplicacao.Servico.sop
 * 
 */
public class ReadAvailableClassesForShift extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static List run(Integer shiftOID) {

	final Shift shift = rootDomainObject.readShiftByOID(shiftOID);
	final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
	final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
	final ExecutionYear executionYear = executionSemester.getExecutionYear();

	final Set<SchoolClass> availableSchoolClasses = new HashSet<SchoolClass>();
	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
	    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
		if (executionDegree.getExecutionYear() == executionYear) {
		    for (final SchoolClass schoolClass : executionDegree.getSchoolClassesSet()) {
			if (schoolClass.getExecutionPeriod() == executionSemester) {
			    if (!shift.getAssociatedClassesSet().contains(schoolClass)) {
				availableSchoolClasses.add(schoolClass);
			    }
			}
		    }
		}
	    }
	}

	final List<InfoClass> infoClasses = new ArrayList<InfoClass>(availableSchoolClasses.size());
	for (final SchoolClass schoolClass : availableSchoolClasses) {
	    final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
	    infoClasses.add(infoClass);
	}

	return infoClasses;
    }

}