package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.EnrolmentRuleServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CreateStudentEnrolmentsWithoutRules extends Service {
    
    public void run(StudentEnrolmentBean enrolmentBean) throws FenixServiceException {
	for (DegreeModuleToEnrol degreeModuleToEnrol: enrolmentBean.getDegreeModulesToEnrol()) {
	    if(enrolmentBean.getInitialCurriculumModules().contains(degreeModuleToEnrol.getCurriculumGroup()) && !enrolmentBean.getCurriculumModules().contains(degreeModuleToEnrol.getCurriculumGroup())) {
		throw new EnrolmentRuleServiceException("error.student.enrolments.invalid.choice");
	    }
	}
	
	for (CurriculumModule curriculumModule : enrolmentBean.getInitialCurriculumModules()) {
	    if(!enrolmentBean.getCurriculumModules().contains(curriculumModule)) {
		try {
		    curriculumModule.delete();
		} catch (DomainException e) {
		    throw new EnrolmentRuleServiceException("error.cannot.delete.curriculumModule", new String[] {curriculumModule.getDegreeModule().getName()});
		}
	    }
	}
	
	enrolmentBean.getStudentCurricularPlan().createModules(enrolmentBean.getDegreeModulesToEnrol(), enrolmentBean.getExecutionPeriod(), EnrollmentCondition.VALIDATED);	
    }

}
