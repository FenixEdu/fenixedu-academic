package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.lang.StringUtils;

public class ExternalDegreeEnrolment extends ExternalDegreeEnrolment_Base {
    
    public ExternalDegreeEnrolment() {
        super();
    }
    
    public ExternalDegreeEnrolment(final StudentCurricularPlan studentCurricularPlan, final CurriculumGroup curriculumGroup,
	    final CurricularCourse curricularCourse, final ExecutionSemester executionSemester,
	    final EnrollmentCondition enrolmentCondition, final String createdBy) {
	
	this();
	checkParameters(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester, enrolmentCondition, createdBy);
	checkInitConstraints(studentCurricularPlan, curricularCourse, executionSemester);
	initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester, enrolmentCondition, createdBy);
    }

    private void checkParameters(final StudentCurricularPlan studentCurricularPlan,
	    final CurriculumGroup curriculumGroup, final CurricularCourse curricularCourse,
	    final ExecutionSemester executionSemester, final EnrollmentCondition enrolmentCondition,
	    final String createdBy) {
	
	if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null
		|| executionSemester == null || enrolmentCondition == null || createdBy == null) {
	    throw new DomainException("error.ExternalDegreeEnrolment.invalid.parameters");
	}
    }
    
    @Override
    public MultiLanguageString getName() {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();
	
	if (!StringUtils.isEmpty(this.getDegreeModule().getName())) {
	    multiLanguageString.setContent(Language.pt, getDegreeModule().getName() + " (" + getDegreeCurricularPlanOfDegreeModule().getName() + ")");
	}
	if (!StringUtils.isEmpty(this.getDegreeModule().getNameEn())) {
	    multiLanguageString.setContent(Language.en, getDegreeModule().getNameEn() + " (" + getDegreeCurricularPlanOfDegreeModule().getName() + ")");
	}
	return multiLanguageString;
    }
    
    @Override
    final public StringBuilder print(String tabs) {
	final StringBuilder builder = new StringBuilder();
	builder.append(tabs);
	builder.append("[E ").append(getDegreeModule().getName()).append(" (");
	builder.append(getDegreeCurricularPlanOfDegreeModule().getName()).append(") ]\n");
	return builder;
    }
    
}
