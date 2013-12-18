package net.sourceforge.fenixedu.domain.util;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Task(englishTitle = "EmailTask")
public class EmailTask extends CronTask {

    private static class SingleEmailDispatcher extends Thread {
        private final String oid;

        protected SingleEmailDispatcher(final Email email) {
            this.oid = email.getExternalId();
        }

        @Atomic
        @Override
        public void run() {
            final Email email = FenixFramework.getDomainObject(oid);
            email.deliver();
        }
    }

    @Override
    public void runTask() {
        for (final Email email : Bennu.getInstance().getEmailQueueSet()) {
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
