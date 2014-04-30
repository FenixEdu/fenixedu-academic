package net.sourceforge.fenixedu.domain.accessControl;

import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

/**
 * A {@link PersistentGroup} which hold a stateless {@link GroupStrategy}.
 * 
 * For each different type of strategy, there must be at most one single instance of this class.
 * 
 * @author João Carvalho (joao.pedro.carvalho@tecnico.ulisboa.pt)
 *
 */
public final class StrategyBasedGroup extends StrategyBasedGroup_Base {

    private StrategyBasedGroup(GroupStrategy strategy) {
        super();
        setStrategy(strategy);
    }

    @Override
    public Group toGroup() {
        return getStrategy();
    }

    static PersistentGroup getInstance(final GroupStrategy strategy) {
        Optional<StrategyBasedGroup> optional = select(strategy);
        return optional.isPresent() ? optional.get() : create(strategy);
    }

    @Atomic
    private static StrategyBasedGroup create(GroupStrategy strategy) {
        Optional<StrategyBasedGroup> optional = select(strategy);
        return optional.isPresent() ? optional.get() : new StrategyBasedGroup(strategy);
    }

    private static Optional<StrategyBasedGroup> select(final GroupStrategy strategy) {
        return filter(StrategyBasedGroup.class).filter(new Predicate<StrategyBasedGroup>() {
            @Override
            public boolean apply(StrategyBasedGroup input) {
                return input.getStrategy().equals(strategy);
            }
        }).first();
    }

}
