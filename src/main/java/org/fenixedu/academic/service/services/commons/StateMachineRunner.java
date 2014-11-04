/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;
import net.sourceforge.fenixedu.domain.util.workflow.StateMachine;
import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StateMachineRunner {

    @Atomic
    public static void run(DefaultRunnerArgs defaultRunnerArgs) {
        StateMachine.execute(defaultRunnerArgs.getState());
    }

    @Atomic
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