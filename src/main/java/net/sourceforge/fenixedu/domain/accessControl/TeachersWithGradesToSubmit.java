package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;

public class TeachersWithGradesToSubmit extends TeachersByExecutionPeriodDegreeAndPlan {

    private static final long serialVersionUID = 1L;

    public TeachersWithGradesToSubmit(ExecutionSemester executionPeriod, Degree degree, DegreeCurricularPlan plan) {
        super(executionPeriod, degree, plan);
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> teachers = super.buildSet();
        for (ExecutionCourse executionCourse : getExecutionPeriod().getExecutionCoursesWithDegreeGradesToSubmit(
                getDegreeCurricularPlan())) {
            for (Professorship professorship : executionCourse.getProfessorships()) {
                if (professorship.getResponsibleFor()) {
                    if (professorship.getPerson() != null) {
                        teachers.add(professorship.getPerson());
                    }
                }
            }
        }
        return teachers;
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            ExecutionSemester period;
            Degree degree;
            DegreeCurricularPlan plan;

            try {
                period = (ExecutionSemester) arguments[0];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(0, ExecutionSemester.class, arguments[0].getClass());
            }

            try {
                degree = (Degree) arguments[1];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(1, Degree.class, arguments[1].getClass());
            }

            try {
                plan = (DegreeCurricularPlan) arguments[2];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(2, DegreeCurricularPlan.class, arguments[2].getClass());
            }
            return new TeachersWithGradesToSubmit(period, degree, plan);
        }

        @Override
        public int getMinArguments() {
            return 3;
        }

        @Override
        public int getMaxArguments() {
            return 3;
        }
    }

    @Override
    public PersistentTeachersWithGradesToSubmitGroup convert() {
        return PersistentTeachersWithGradesToSubmitGroup.getInstance(getExecutionPeriod(), getDegreeCurricularPlan());
    }
}
