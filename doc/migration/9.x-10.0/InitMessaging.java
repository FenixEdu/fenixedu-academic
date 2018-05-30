package pt.ist.fenix.webapp.task;
	

import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.messaging.core.domain.MessagingSystem;
import pt.ist.fenixframework.Atomic;


public class InitMessagingSystem extends CustomTask{
    @Override public Atomic.TxMode getTxMode() {
        return Atomic.TxMode.WRITE;
    }


    @Override public void runTask() throws Exception {
        MessagingSystem.getInstance();
    }
}