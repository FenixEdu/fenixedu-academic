package net.sourceforge.fenixedu.domain.util;

import jvstm.TransactionalCommand;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.pstm.Transaction;

public class EmailTask extends EmailTask_Base {

    private static class SingleEmailDispatcher extends Thread {
        private final String oid;

        protected SingleEmailDispatcher(final Email email) {
            this.oid = email.getExternalId();
        }

        @Override
        public void run() {
            try {
                Transaction.withTransaction(false, new TransactionalCommand() {
                    @Override
                    public void doIt() {
                        final Email email = DomainObject.fromExternalId(oid);
                        email.deliver();
                    }
                });
            } finally {
                Transaction.forceFinish();
            }
        }
    }

    @Override
    public void runTask() {
        for (final Email email : RootDomainObject.getInstance().getEmailQueueSet()) {
            final SingleEmailDispatcher emailDispatcher = new SingleEmailDispatcher(email);
            emailDispatcher.start();
            try {
                emailDispatcher.join();
            } catch (final InterruptedException e) {
                throw new Error(e);
            }
        }
    }

}
