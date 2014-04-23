package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class ResponsibleProfessorshipsForCurrentYear extends Group {

    @Override
    public Set<Person> getElements() {
        final Set<Person> result = new HashSet<Person>();
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
                for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                    if (professorship.isResponsibleFor()) {
                        result.add(professorship.getPerson());
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean isMember(final Person person) {
        if (person != null) {
            for (final Professorship professorship : person.getProfessorshipsSet()) {
                if (professorship.isResponsibleFor()) {
                    final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                    final ExecutionSemester executionPeriod = executionCourse.getExecutionPeriod();
                    final ExecutionYear executionYear = executionPeriod.getExecutionYear();
                    if (executionYear.isCurrent()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return null;
    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        return PersistentProfessorshipsGroup.getInstance(false, AcademicPeriod.YEAR);
    }
}
