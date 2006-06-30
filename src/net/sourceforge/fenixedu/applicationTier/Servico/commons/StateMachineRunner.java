/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.util.IState;
import net.sourceforge.fenixedu.domain.util.StateMachine;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StateMachineRunner extends Service {

    public void run(IState state) {
        StateMachine.execute(state);
    }

}
