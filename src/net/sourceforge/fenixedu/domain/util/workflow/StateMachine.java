package net.sourceforge.fenixedu.domain.util.workflow;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StateMachine {

	public static IState execute(IState state) {
		state.checkConditionsToForward();
		return state.nextState();
	}

	public static IState execute(final IState state, final StateBean bean) {
		final String nextState = bean.getNextState();
		if (state.getValidNextStates().contains(nextState)) {
			state.checkConditionsToForward(bean);
			return state.nextState(bean);
		} else {
			throw new DomainException("error.invalid.next.state");
		}
	}

}
