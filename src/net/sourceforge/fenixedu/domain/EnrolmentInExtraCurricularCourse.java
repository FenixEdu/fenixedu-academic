package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.util.EnrolmentAction;


/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class EnrolmentInExtraCurricularCourse extends EnrolmentInExtraCurricularCourse_Base {

	protected EnrolmentInExtraCurricularCourse() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public EnrolmentInExtraCurricularCourse(StudentCurricularPlan studentCurricularPlan,
            CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
            EnrollmentCondition enrolmentCondition, String createdBy) {
    	this();
    	initializeAsNew(studentCurricularPlan, curricularCourse,
                executionPeriod, enrolmentCondition, createdBy);
    	createEnrolmentLog(EnrolmentAction.ENROL);
    }

}