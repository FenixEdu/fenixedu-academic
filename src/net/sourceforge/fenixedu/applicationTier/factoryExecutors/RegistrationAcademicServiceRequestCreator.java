package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.CourseGroupChangeRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRevisionRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.ExtraExamRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.FreeSolicitationAcademicRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.StudentReingressionRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.Checked;

public class RegistrationAcademicServiceRequestCreator extends RegistrationAcademicServiceRequestCreateBean implements FactoryExecutor {

    public RegistrationAcademicServiceRequestCreator(final Registration registration) {
	super(registration);
    }

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    public Object execute() {
	final Object result;
	switch (getAcademicServiceRequestType()) {
	case REINGRESSION:
	    result = new StudentReingressionRequest(getRegistration(), ExecutionYear.readCurrentExecutionYear());
	    break;

	case EQUIVALENCE_PLAN:
	    result = new EquivalencePlanRequest(getRegistration(), getExecutionYear());
	    break;

	case REVISION_EQUIVALENCE_PLAN:
	    result = new EquivalencePlanRevisionRequest(getRegistration(), getExecutionYear());
	    break;

	case COURSE_GROUP_CHANGE_REQUEST:
	    result = new CourseGroupChangeRequest(getRegistration(), getCurriculumGroup(), getCourseGroup(), ExecutionYear.readCurrentExecutionYear());
	    break;

	case EXTRA_EXAM_REQUEST:
	    result = new ExtraExamRequest(getRegistration(), getEnrolment(), ExecutionYear.readCurrentExecutionYear());
	    break;

	case FREE_SOLICITATION_ACADEMIC_REQUEST:
	    result = new FreeSolicitationAcademicRequest(getRegistration(), ExecutionYear.readCurrentExecutionYear(), getPurpose());
	    break;
	    
	default:
	    result = null;
	}
	return result;
    }

}
