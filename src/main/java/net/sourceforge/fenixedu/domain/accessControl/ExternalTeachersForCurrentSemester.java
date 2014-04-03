package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherAuthorization;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class ExternalTeachersForCurrentSemester extends Group {

    @Override
    public Set<Person> getElements() {
        final Set<Person> result = new HashSet<Person>();
        final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        for (final TeacherAuthorization teacherAuthorization : executionSemester.getAuthorizationSet()) {
            if (teacherAuthorization instanceof ExternalTeacherAuthorization) {
                final Teacher teacher = teacherAuthorization.getTeacher();
                result.add(teacher.getPerson());
            }
        }
        return result;
    }

    @Override
    public boolean isMember(final Person person) {
        if (person != null) {
            for (final TeacherAuthorization teacherAuthorization : person.getTeacherAuthorizationsAuthorizedSet()) {
                if (teacherAuthorization instanceof ExternalTeacherAuthorization) {
                    final ExternalTeacherAuthorization externalTeacherAuthorization =
                            (ExternalTeacherAuthorization) teacherAuthorization;
                    if (externalTeacherAuthorization.getExecutionSemester().isCurrent()) {
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
    public PersistentProfessorshipsGroup convert() {
        return PersistentProfessorshipsGroup.getInstance(true, AcademicPeriod.SEMESTER);
    }
}
