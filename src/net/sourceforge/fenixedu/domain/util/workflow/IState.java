package net.sourceforge.fenixedu.domain.util.workflow;

import java.util.Set;

public interface IState {

    public IState nextState();

    public IState nextState(final StateBean bean);

    public void checkConditionsToForward();

    public void checkConditionsToForward(final StateBean bean);

    public Set<String> getValidNextStates();

}
