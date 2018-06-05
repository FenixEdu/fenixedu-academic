package org.fenixedu.academic.task;
		
	

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.messaging.core.domain.MessageStoragePolicy;
import org.fenixedu.messaging.core.domain.Sender;
import pt.ist.fenixframework.Atomic;



public class SetSenderKeepAllPolicyTask extends CustomTask {
    @Override public Atomic.TxMode getTxMode() {
        return Atomic.TxMode.WRITE;
    }



    @Override public void runTask() throws Exception {
        for (Sender sender : Bennu.getInstance().getMessagingSystem().getSenderSet()) {
            sender.setPolicy(MessageStoragePolicy.keepAll());
        }
    }
}