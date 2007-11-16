package net.sourceforge.fenixedu.domain.util;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.workflow.IState;

public class StateMachine {
    public static IState execute(IState state) {
	state.checkConditionsToForward();
	return state.nextState();
    }

    public static IState execute(IState state, String nextState) {
	if (state.getValidNextStates().contains(nextState)) {
	    state.checkConditionsToForward(nextState);
	    return state.nextState(nextState);
	} else {
	    throw new DomainException("error.invalid.next.state");
	}
    }
}
