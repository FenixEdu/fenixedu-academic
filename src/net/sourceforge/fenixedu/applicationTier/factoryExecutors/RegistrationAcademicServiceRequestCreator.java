package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.CourseGroupChangeRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRevisionRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.ExtraExamRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.FreeSolicitationAcademicRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.PartialRegistrationRegimeRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.SpecialSeasonRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.StudentReingressionRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.PhotocopyRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class RegistrationAcademicServiceRequestCreator extends RegistrationAcademicServiceRequestCreateBean implements
	FactoryExecutor {

    public RegistrationAcademicServiceRequestCreator(final Registration registration) {
	super(registration);
    }

    @Service
    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    public Object execute() {
	final Object result;
	switch (getAcademicServiceRequestType()) {
	case REINGRESSION:
	    result = new StudentReingressionRequest(this);
	    break;

	case EQUIVALENCE_PLAN:
	    result = new EquivalencePlanRequest(this);
	    break;

	case REVISION_EQUIVALENCE_PLAN:
	    result = new EquivalencePlanRevisionRequest(this);
	    break;

	case COURSE_GROUP_CHANGE_REQUEST:
	    this.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	    result = new CourseGroupChangeRequest(this);
	    break;

	case EXTRA_EXAM_REQUEST:
	    this.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	    result = new ExtraExamRequest(this);
	    break;

	case FREE_SOLICITATION_ACADEMIC_REQUEST:
	    result = new FreeSolicitationAcademicRequest(this);
	    break;

	case SPECIAL_SEASON_REQUEST:
	    result = new SpecialSeasonRequest(this);
	    break;

	case PHOTOCOPY_REQUEST:
	    this.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	    result = new PhotocopyRequest(this);
	    break;

	case PARTIAL_REGIME_REQUEST:
	    result = new PartialRegistrationRegimeRequest(this);
	    break;

	default:
	    throw new DomainException("error.RegistrationAcademicServiceRequestCreator.no.executor");
	}

	return result;
    }

}
