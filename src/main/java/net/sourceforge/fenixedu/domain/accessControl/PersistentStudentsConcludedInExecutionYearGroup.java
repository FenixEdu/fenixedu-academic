package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Objects;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentStudentsConcludedInExecutionYearGroup extends PersistentStudentsConcludedInExecutionYearGroup_Base {
    protected PersistentStudentsConcludedInExecutionYearGroup(Degree degree, ExecutionYear conclusionYear) {
        super();
        setDegree(degree);
        setConclusionYear(conclusionYear);
    }

    @Override
    public Group toGroup() {
        return StudentsConcludedInExecutionYearGroup.get(getDegree(), getConclusionYear());
    }

    @Override
    protected void gc() {
        setDegree(null);
        setConclusionYear(null);
        super.gc();
    }

    public static PersistentStudentsConcludedInExecutionYearGroup getInstance(Degree degree, ExecutionYear conclusionYear) {
        return singleton(
                () -> conclusionYear.getStudentsConcludedInExecutionYearGroupSet().stream()
                .filter(group -> Objects.equals(group.getDegree(), degree)).findAny(),
                () -> new PersistentStudentsConcludedInExecutionYearGroup(degree, conclusionYear));
    }
}
