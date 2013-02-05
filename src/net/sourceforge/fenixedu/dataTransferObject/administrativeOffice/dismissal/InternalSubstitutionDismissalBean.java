package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

public class InternalSubstitutionDismissalBean extends DismissalBean {

    static private final long serialVersionUID = 1L;

    @Override
    public Collection<? extends CurricularCourse> getAllCurricularCoursesToDismissal() {

        final Collection<CurricularCourse> result = new HashSet<CurricularCourse>();

        final StudentCurricularPlan scp = getStudentCurricularPlan();
        final Collection<CycleType> cyclesToEnrol = scp.getDegreeType().getSupportedCyclesToEnrol();

        if (cyclesToEnrol.isEmpty()) {

            for (final CurricularCourse curricularCourse : scp.getDegreeCurricularPlan().getCurricularCoursesSet()) {
                if (curricularCourse.isActive(getExecutionPeriod()) && !isApproved(scp, curricularCourse)) {
                    result.add(curricularCourse);
                }
            }

        } else {

            for (final CycleType cycleType : cyclesToEnrol) {
                final CourseGroup courseGroup = getCourseGroupWithCycleTypeToCollectCurricularCourses(scp, cycleType);
                if (courseGroup != null) {
                    for (final CurricularCourse curricularCourse : courseGroup.getAllCurricularCourses(getExecutionPeriod())) {
                        if (!isApproved(scp, curricularCourse)) {
                            result.add(curricularCourse);
                        }
                    }
                }
            }

        }

        return result;
    }

    /**
     * Do not use isApproved from StudentCurricularPlan, because that method
     * also check global equivalences, and in internal substitution we can not
     * check that.
     */
    private boolean isApproved(final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse) {
        for (final Enrolment enrolment : studentCurricularPlan.getEnrolments()) {
            if (enrolment.getCurricularCourse().isEquivalent(curricularCourse) && enrolment.isApproved()) {
                return true;
            }
        }
        return false;
    }

    private CourseGroup getCourseGroupWithCycleTypeToCollectCurricularCourses(final StudentCurricularPlan studentCurricularPlan,
            final CycleType cycleType) {

        final CycleCurriculumGroup curriculumGroup = studentCurricularPlan.getCycle(cycleType);
        return curriculumGroup != null ? curriculumGroup.getDegreeModule() : studentCurricularPlan.getDegreeCurricularPlan()
                .getCycleCourseGroup(cycleType);

    }

}
