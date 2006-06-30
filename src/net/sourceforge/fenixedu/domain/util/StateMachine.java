package net.sourceforge.fenixedu.domain.util;

public class StateMachine {
	public static void execute(IState state) {
		state.checkConditionsToForward();
		state.nextState();
	}
}
