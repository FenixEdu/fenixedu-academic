package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public abstract class PersistentTeachersWithIncompleteEvaluationWorkGroup extends
        PersistentTeachersWithIncompleteEvaluationWorkGroup_Base {
    protected PersistentTeachersWithIncompleteEvaluationWorkGroup() {
        super();
    }

    protected void init(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        setPeriod(period);
        setDegreeCurricularPlan(degreeCurricularPlan);
    }

    @Override
    protected void gc() {
        setPeriod(null);
        setDegreeCurricularPlan(null);
        super.gc();
    }

    protected static <T extends PersistentTeachersWithIncompleteEvaluationWorkGroup> T singleton(Class<T> type,
            ExecutionSemester period, final DegreeCurricularPlan degreeCurricularPlan, Supplier<T> creator) {
        return singleton(
                () -> ((Optional<T>) period.getTeachersWithIncompleteEvaluationWorkGroupSet().stream()
                        .filter(group -> Objects.equals(group.getDegreeCurricularPlan(), degreeCurricularPlan)).findAny()),
                creator);
    }
}
