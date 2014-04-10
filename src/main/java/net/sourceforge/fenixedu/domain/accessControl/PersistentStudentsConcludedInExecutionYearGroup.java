package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

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
        PersistentStudentsConcludedInExecutionYearGroup instance = select(degree, conclusionYear);
        return instance != null ? instance : create(degree, conclusionYear);
    }

    private static PersistentStudentsConcludedInExecutionYearGroup create(Degree degree, ExecutionYear conclusionYear) {
        PersistentStudentsConcludedInExecutionYearGroup instance = select(degree, conclusionYear);
        return instance != null ? instance : new PersistentStudentsConcludedInExecutionYearGroup(degree, conclusionYear);
    }

    private static PersistentStudentsConcludedInExecutionYearGroup select(Degree degree, ExecutionYear conclusionYear) {
        Set<PersistentStudentsConcludedInExecutionYearGroup> candidates =
                conclusionYear.getStudentsConcludedInExecutionYearGroupSet();
        for (PersistentStudentsConcludedInExecutionYearGroup group : candidates) {
            if (group.getDegree().equals(degree)) {
                return group;
            }
        }
        return null;
    }
}
