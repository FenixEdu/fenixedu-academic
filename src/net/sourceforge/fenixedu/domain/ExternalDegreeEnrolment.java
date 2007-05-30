package net.sourceforge.fenixedu.domain;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ExternalDegreeEnrolment extends ExternalDegreeEnrolment_Base {
    
    protected ExternalDegreeEnrolment() {
        super();
    }
    
    public ExternalDegreeEnrolment(final StudentCurricularPlan studentCurricularPlan, final CurriculumGroup curriculumGroup,
	    final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod,
	    final EnrollmentCondition enrolmentCondition, final String createdBy) {
	
	this();
	checkParameters(studentCurricularPlan, curriculumGroup, curricularCourse, executionPeriod, enrolmentCondition, createdBy);
	checkInitConstraints(studentCurricularPlan, curricularCourse, executionPeriod);
	initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionPeriod, enrolmentCondition, createdBy);
    }

    private void checkParameters(final StudentCurricularPlan studentCurricularPlan,
	    final CurriculumGroup curriculumGroup, final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod, final EnrollmentCondition enrolmentCondition,
	    final String createdBy) {
	
	if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null
		|| executionPeriod == null || enrolmentCondition == null || createdBy == null) {
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
    public StringBuilder print(String tabs) {
	final StringBuilder builder = new StringBuilder();
	builder.append(tabs);
	builder.append("[E ").append(getDegreeModule().getName()).append(" (");
	builder.append(getDegreeCurricularPlanOfDegreeModule().getName()).append(") ]\n");

	return builder;
    }

}
