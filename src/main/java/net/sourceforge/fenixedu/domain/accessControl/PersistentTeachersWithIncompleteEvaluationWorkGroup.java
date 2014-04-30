package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

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

    protected static <T extends PersistentTeachersWithIncompleteEvaluationWorkGroup> T getInstance(Class<T> type,
            ExecutionSemester period, final DegreeCurricularPlan degreeCurricularPlan, Supplier<T> maker) {
        Optional<T> instance = select(type, period, degreeCurricularPlan);
        return instance.isPresent() ? instance.get() : transactionalMake(type, period, degreeCurricularPlan, maker);
    }

    @Atomic(mode = TxMode.WRITE)
    private static <T extends PersistentTeachersWithIncompleteEvaluationWorkGroup> T transactionalMake(Class<T> type,
            ExecutionSemester period, final DegreeCurricularPlan degreeCurricularPlan, Supplier<? extends T> maker) {
        Optional<T> instance = select(type, period, degreeCurricularPlan);
        return instance.or(maker);
    }

    private static <T extends PersistentTeachersWithIncompleteEvaluationWorkGroup> Optional<T> select(Class<T> type,
            ExecutionSemester period, final DegreeCurricularPlan degreeCurricularPlan) {
        FluentIterable<T> byPeriod = FluentIterable.from(period.getTeachersWithIncompleteEvaluationWorkGroupSet()).filter(type);
        return Iterables.tryFind(byPeriod, new Predicate<T>() {
            @Override
            public boolean apply(T group) {
                return Objects.equal(group.getDegreeCurricularPlan(), degreeCurricularPlan);
            }
        });
    }
}
