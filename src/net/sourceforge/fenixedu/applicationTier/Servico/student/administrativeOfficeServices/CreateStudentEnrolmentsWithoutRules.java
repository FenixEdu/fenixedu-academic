package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CreateStudentEnrolmentsWithoutRules extends Service {
    
    public void run(StudentEnrolmentBean enrolmentBean) throws FenixServiceException {
	for (DegreeModuleToEnrol degreeModuleToEnrol: enrolmentBean.getDegreeModulesToEnrol()) {
	    if(enrolmentBean.getInitialCurriculumModules().contains(degreeModuleToEnrol.getCurriculumGroup()) && !enrolmentBean.getCurriculumModules().contains(degreeModuleToEnrol.getCurriculumGroup())) {
		throw new FenixServiceException("student.enrolments.invalid.choice");
	    }
	}
	
	for (CurriculumModule curriculumModule : enrolmentBean.getInitialCurriculumModules()) {
	    if(!enrolmentBean.getCurriculumModules().contains(curriculumModule)) {
		curriculumModule.delete();
	    }
	}
	
	
	
	
    }

}
