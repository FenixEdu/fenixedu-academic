package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
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

    @CustomGroupArgument(index = 1)
    public static Argument<ExecutionSemester> executionSemesterArgument() {
        return new SimpleArgument<ExecutionSemester, PersistentTeachersWithIncompleteEvaluationWorkGroup>() {
            @Override
            public ExecutionSemester parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<ExecutionSemester> getDomainObject(argument);
            }

            @Override
            public Class<? extends ExecutionSemester> getType() {
                return ExecutionSemester.class;
            }

            @Override
            public String extract(PersistentTeachersWithIncompleteEvaluationWorkGroup group) {
                return group.getPeriod() != null ? group.getPeriod().getExternalId() : "";
            }
        };
    }

    @CustomGroupArgument(index = 2)
    public static Argument<DegreeCurricularPlan> degreeCurricularPlanArgument() {
        return new SimpleArgument<DegreeCurricularPlan, PersistentTeachersWithIncompleteEvaluationWorkGroup>() {
            @Override
            public DegreeCurricularPlan parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<DegreeCurricularPlan> getDomainObject(argument);
            }

            @Override
            public Class<? extends DegreeCurricularPlan> getType() {
                return DegreeCurricularPlan.class;
            }

            @Override
            public String extract(PersistentTeachersWithIncompleteEvaluationWorkGroup group) {
                return group.getDegreeCurricularPlan() != null ? group.getDegreeCurricularPlan().getExternalId() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getDegreeCurricularPlan().getPresentationName(), getPeriod().getName() };
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
