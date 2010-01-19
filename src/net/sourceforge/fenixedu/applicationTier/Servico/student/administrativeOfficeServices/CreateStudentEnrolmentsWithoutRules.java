package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.EnrolmentRuleServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateStudentEnrolmentsWithoutRules extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(StudentEnrolmentBean enrolmentBean) throws FenixServiceException {
	Set<CurriculumModule> initialCurriculumModules = enrolmentBean.getInitialCurriculumModules();
	for (DegreeModuleToEnrol degreeModuleToEnrol : enrolmentBean.getDegreeModulesToEnrol()) {
	    if (initialCurriculumModules.contains(degreeModuleToEnrol.getCurriculumGroup())
		    && !enrolmentBean.getCurriculumModules().contains(degreeModuleToEnrol.getCurriculumGroup())) {
		throw new EnrolmentRuleServiceException("error.student.enrolments.invalid.choice");
	    }
	}

	for (CurriculumModule curriculumModule : initialCurriculumModules) {
	    if (!enrolmentBean.getCurriculumModules().contains(curriculumModule)) {
		try {
		    curriculumModule.delete();
		} catch (DomainException e) {
		    throw new EnrolmentRuleServiceException("error.cannot.delete.curriculumModule",
			    new String[] { curriculumModule.getDegreeModule().getName() });
		}
	    }
	}

	enrolmentBean.getStudentCurricularPlan().createModules(enrolmentBean.getDegreeModulesToEnrol(),
		enrolmentBean.getExecutionPeriod(), EnrollmentCondition.VALIDATED);
    }

}