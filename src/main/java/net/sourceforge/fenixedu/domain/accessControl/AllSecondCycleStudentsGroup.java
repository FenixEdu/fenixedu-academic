package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.fenixedu.bennu.core.domain.Bennu;

public class AllSecondCycleStudentsGroup extends Group {

    private static final long serialVersionUID = 1L;

    public AllSecondCycleStudentsGroup() {
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = new HashSet<Person>();

        for (final Degree degree : Bennu.getInstance().getDegreesSet()) {
            if (degree.isBolonhaDegree() && degree.getDegreeType().hasCycleTypes(CycleType.SECOND_CYCLE)) {
                for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
                    if (degreeCurricularPlan.isActive()) {
                        for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan
                                .getStudentCurricularPlansSet()) {
                            if (studentCurricularPlan.isActive()) {
                                final CycleCurriculumGroup cycleCurriculumGroup =
                                        studentCurricularPlan.getCycle(CycleType.SECOND_CYCLE);
                                if (cycleCurriculumGroup != null && !cycleCurriculumGroup.isConcluded()) {
                                    final CycleCurriculumGroup firstCycleCurriculumGroup =
                                            studentCurricularPlan.getCycle(CycleType.FIRST_CYCLE);
                                    if (firstCycleCurriculumGroup == null || firstCycleCurriculumGroup.isConcluded()) {
                                        elements.add(studentCurricularPlan.getPerson());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return elements;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return null;
    }

    @Override
    public PersistentStudentGroup convert() {
        return PersistentStudentGroup.getInstance(CycleType.SECOND_CYCLE);
    }
}
