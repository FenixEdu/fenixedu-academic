/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.NullArgumentException;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class InquiriesRegistry extends InquiriesRegistry_Base {

    public InquiriesRegistry() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setState(InquiriesRegistryState.ANSWER_LATER);
    }

    public InquiriesRegistry(ExecutionCourse executionCourse, ExecutionSemester executionSemester, Registration registration) {
	this();
	checkParameters(executionCourse, executionSemester, registration);
	this.setExecutionCourse(executionCourse);
	this.setExecutionPeriod(executionSemester);
	this.setStudent(registration);

    }
    
    public InquiriesRegistry(ExecutionCourse executionCourse, CurricularCourse curricularCourse,
	    ExecutionSemester executionSemester, Registration registration) {
	this(executionCourse, executionSemester, registration);
	if (curricularCourse == null) {
	    throw new NullArgumentException("CurricularCourse should not be null!");
	}	
	this.setCurricularCourse(curricularCourse);
    }

    private void checkParameters(ExecutionCourse executionCourse, ExecutionSemester executionSemester, Registration registration) {
	if ((executionCourse == null) || (executionSemester == null) || (registration == null)) {
	    throw new NullArgumentException("The executionCourse, executionPeriod and student should not be null!");
	}

	for (final InquiriesRegistry inquiriesRegistry : registration.getAssociatedInquiriesRegistries()) {
	    if (inquiriesRegistry.getExecutionCourse() == executionCourse
		    && inquiriesRegistry.getExecutionPeriod() == executionSemester) {
		throw new DomainException(
			"Already exists an Inquiries Registry in this Registration for the given period and course!");
	    }
	}
    }

    public boolean isAnswered() {
	return getState() == InquiriesRegistryState.ANSWERED;
    }

    public boolean isNotAnswered() {
	return getState() == InquiriesRegistryState.NOT_ANSWERED;
    }

    public boolean isToAnswerLater() {
	return !isNotAvailableToInquiries() && (getState() == null || getState() == InquiriesRegistryState.ANSWER_LATER);
    }

    public boolean isNotAvailableToInquiries() {
	return !getExecutionCourse().getAvailableForInquiries();
    }

    public ExecutionDegree getExecutionDegree() {

	final StudentCurricularPlan studentCurricularPlan = getStudent().getActiveStudentCurricularPlan();
	if (studentCurricularPlan != null) {
	    final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
	    return degreeCurricularPlan.getExecutionDegreeByYear(getExecutionPeriod().getExecutionYear());
	}

	DegreeCurricularPlan lastDegreeCurricularPlan = getStudent().getLastDegreeCurricularPlan();
	if (lastDegreeCurricularPlan != null) {
	    return lastDegreeCurricularPlan.getExecutionDegreeByYear(getExecutionPeriod().getExecutionYear());
	}

	return null;
    }

    public InquiriesStudentExecutionPeriod getInquiriesStudentExecutionPeriod() {
	return getStudent().getStudent().getInquiriesStudentExecutionPeriod(getExecutionPeriod());
    }

}
