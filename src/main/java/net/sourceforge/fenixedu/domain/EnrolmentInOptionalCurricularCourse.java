package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.util.EnrolmentAction;

/**
 * @author dcs-rjao
 * 
 *         24/Mar/2003
 * 
 * 
 * @deprecated use OptionalEnrolment
 * 
 */
@Deprecated
public class EnrolmentInOptionalCurricularCourse extends EnrolmentInOptionalCurricularCourse_Base {

    protected EnrolmentInOptionalCurricularCourse() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public EnrolmentInOptionalCurricularCourse(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition, String createdBy) {
        this();
        initializeAsNew(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition, createdBy);
        createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    @Override
    final public boolean isOptional() {
        return true;
    }

    // new student structure methods
    public EnrolmentInOptionalCurricularCourse(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            CurricularCourse curricularCourse, ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition,
            String createdBy) {
        this();
        if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null || executionSemester == null
                || enrolmentCondition == null || createdBy == null) {
            throw new DomainException("invalid arguments");
        }
        // TODO: check this
        // validateDegreeModuleLink(curriculumGroup, curricularCourse);
        initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester, enrolmentCondition,
                createdBy);
    }
}