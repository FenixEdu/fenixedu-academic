package net.sourceforge.fenixedu.domain.util.workflow;

import java.util.Set;

public interface IState {
    public IState nextState();

    public IState nextState(String nextState);

    public void checkConditionsToForward();

    public void checkConditionsToForward(String nextState);

    public Set<String> getValidNextStates();

}
