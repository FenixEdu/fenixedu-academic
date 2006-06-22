package net.sourceforge.fenixedu.domain.util;

public class StateMachine {
	public void execute(IState state) {
		state.checkConditionsToForward();
		state.nextState();
	}
}
