package net.sourceforge.fenixedu.domain.accessControl.groups;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.LeafGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersistentStudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.student.Student;

public class StudentsByDegreeAndCurricularYear extends LeafGroup {

    private final Degree degree;

    private final CurricularYear curricularYear;

    private final ExecutionYear executionYear;

    public StudentsByDegreeAndCurricularYear(final Degree degree, final CurricularYear curricularYear,
            final ExecutionYear executionYear) {
        this.degree = degree;
        this.executionYear = executionYear;
        this.curricularYear = curricularYear;
    }

    @Override
    public Set<Person> getElements() {
        final Set<Person> persons = new HashSet<Person>();
        for (Student student : getDegree().getStudentsFromGivenCurricularYear(getCurricularYear().getYear(), getExecutionYear())) {
            persons.add(student.getPerson());
        }
        return persons;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getDegree()), new OidOperator(getCurricularYear()),
                new OidOperator(getExecutionYear()) };
    }

    public Degree getDegree() {
        return (degree);
    }

    public CurricularYear getCurricularYear() {
        return (curricularYear);
    }

    public ExecutionYear getExecutionYear() {
        return (executionYear);
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            return new StudentsByDegreeAndCurricularYear((Degree) arguments[0], (CurricularYear) arguments[1],
                    (ExecutionYear) arguments[2]);
        }

        @Override
        public int getMaxArguments() {
            return 3;
        }

        @Override
        public int getMinArguments() {
            return 3;
        }

    }

    @Override
    public PersistentStudentGroup convert() {
        return PersistentStudentGroup.getInstance(getDegree(), getCurricularYear(), getExecutionYear());
    }
}
