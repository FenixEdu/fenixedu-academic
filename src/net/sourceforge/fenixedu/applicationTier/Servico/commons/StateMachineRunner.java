/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;
import net.sourceforge.fenixedu.domain.util.workflow.StateMachine;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StateMachineRunner extends FenixService {

    @Service
    public static void run(DefaultRunnerArgs defaultRunnerArgs) {
        StateMachine.execute(defaultRunnerArgs.getState());
    }

    @Service
    public static void run(RunnerArgs runnerArgs) {
        StateMachine.execute(runnerArgs.getState(), new StateBean(runnerArgs.getNextState()));
    }

    public static class DefaultRunnerArgs {
        private IState state;

        public DefaultRunnerArgs(IState state) {
            this.state = state;
        }

        public IState getState() {
            return state;
        }

        public void setState(IState state) {
            this.state = state;
        }
    }

    public static class RunnerArgs {
        private IState state;
        private String nextState;

        public RunnerArgs(IState state, String nextState) {
            this.state = state;
            this.nextState = nextState;
        }

        public String getNextState() {
            return nextState;
        }

        public void setNextState(String nextState) {
            this.nextState = nextState;
        }

        public IState getState() {
            return state;
        }

        public void setState(IState state) {
            this.state = state;
        }

    }

}