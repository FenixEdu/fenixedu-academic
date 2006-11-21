package net.sourceforge.fenixedu.domain.util.workflow;

import java.util.Set;

public interface IState {
    public void nextState();

    public void nextState(String nextState);

    public void checkConditionsToForward();

    public void checkConditionsToForward(String nextState);

    public Set<String> getValidNextStates();

}
