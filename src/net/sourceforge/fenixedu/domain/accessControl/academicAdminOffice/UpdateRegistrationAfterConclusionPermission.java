package net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

public class UpdateRegistrationAfterConclusionPermission extends UpdateRegistrationAfterConclusionPermission_Base {

    private UpdateRegistrationAfterConclusionPermission() {
	super();
    }

    UpdateRegistrationAfterConclusionPermission(final AdministrativeOfficePermissionGroup group, final PermissionType type) {
	this();
	init(group, type);
    }

    @Override
    public boolean isAppliable(final DomainObject obj) {
	if (obj instanceof Registration) {

	    final Registration registration = (Registration) obj;
	    return !registration.getDegree().isEmpty() && registration.isRegistrationConclusionProcessed();

	} else if (obj instanceof StudentCurricularPlan) {

	    final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) obj;
	    return !studentCurricularPlan.isEmptyDegree() && studentCurricularPlan.isConclusionProcessed();

	} else if (obj instanceof CycleCurriculumGroup) {

	    return ((CycleCurriculumGroup) obj).isConclusionProcessed();

	} else {

	    return false;
	}
    }

}
