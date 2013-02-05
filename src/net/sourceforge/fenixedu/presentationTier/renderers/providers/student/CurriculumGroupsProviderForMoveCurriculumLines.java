package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.CurriculumLineLocationBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class CurriculumGroupsProviderForMoveCurriculumLines implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final CurriculumLineLocationBean bean = (CurriculumLineLocationBean) source;

        final Student student = bean.getStudent();
        final Set<CurriculumGroup> result = new HashSet<CurriculumGroup>();

        for (final Registration registration : student.getRegistrations()) {

            if (!registration.isBolonha()) {
                result.addAll(registration.getLastStudentCurricularPlan().getAllCurriculumGroups());
                continue;
            }

            final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
            result.addAll(studentCurricularPlan.getNoCourseGroupCurriculumGroups());

            for (final CycleCurriculumGroup cycle : studentCurricularPlan.getCycleCurriculumGroups()) {

                if (bean.isWithRules() && isConcluded(student, cycle)) {
                    continue;
                }

                result.addAll(cycle.getAllCurriculumGroups());
            }
        }

        final Set<AcademicProgram> programs =
                AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
                        AcademicOperationType.STUDENT_ENROLMENTS);

        return Collections2.filter(result, new Predicate<CurriculumGroup>() {
            @Override
            public boolean apply(CurriculumGroup group) {

                return programs.contains(group.getDegreeCurricularPlanOfStudent().getDegree());
            }
        });
    }

    private boolean isConcluded(final Student student, final CycleCurriculumGroup cycle) {
        return cycle.hasConclusionProcess()
                || (cycle.isExternal() && student.hasRegistrationFor(cycle.getDegreeCurricularPlanOfDegreeModule()));
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
