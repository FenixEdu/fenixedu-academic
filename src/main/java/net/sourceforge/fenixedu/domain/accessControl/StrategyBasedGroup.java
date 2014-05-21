package net.sourceforge.fenixedu.domain.accessControl;

import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;

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
        return singleton(
                () -> filter(StrategyBasedGroup.class).filter(group -> group.getStrategy().equals(strategy)).findAny(),
                () -> new StrategyBasedGroup(strategy));
    }
}
