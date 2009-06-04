package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;

public class CoordinatorExecutionDegreeCoursesReport extends CoordinatorExecutionDegreeCoursesReport_Base {

    public CoordinatorExecutionDegreeCoursesReport() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    private CoordinatorExecutionDegreeCoursesReport(final ExecutionDegree executionDegree,
	    final ExecutionInterval executionInterval) {
	this();

	if (executionDegree.getExecutionDegreeCoursesReports(executionInterval) != null) {
	    throw new DomainException(
		    "error.CoordinatorExecutionDegreeCoursesReport.alreadyExistsForDegreeAndIntervaAndCoordinator");
	}

	final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(AccessControl.getPerson());
	if (coordinator == null || !coordinator.isResponsible()) {
	    throw new DomainException("error.CoordinatorExecutionDegreeCoursesReport.isNotResponsible");
	}
	setCoordinator(coordinator);
	setExecutionDegree(executionDegree);
	setExecutionInterval(executionInterval);
    }

    @Service
    public static CoordinatorExecutionDegreeCoursesReport makeNew(final ExecutionDegree executionDegree,
	    final ExecutionInterval executionInterval) {
	return new CoordinatorExecutionDegreeCoursesReport(executionDegree, executionInterval);
    }

    public boolean isEmpty() {
	return StringUtils.isEmpty(getStepsToImproveResults())
		&& StringUtils.isEmpty(getBolonhaProcessImplementationReflection()) && StringUtils.isEmpty(getGlobalComment())
		&& StringUtils.isEmpty(getTeachingBestPracticesDevelopedByTeachers());
    }

}
