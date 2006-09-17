package net.sourceforge.fenixedu.domain.util.workflow;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StateMachine {
	public static void execute(IState state) {
		state.checkConditionsToForward();
		state.nextState();
	}
    
    public static void execute(IState state, String nextState) {
        if(state.getValidNextStates().contains(nextState)){
            state.checkConditionsToForward(nextState);
            state.nextState(nextState);
        } else {
            throw new DomainException("invalid next state");
        }
    }
}
