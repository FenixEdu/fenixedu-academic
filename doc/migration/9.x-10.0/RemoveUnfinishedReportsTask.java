package org.fenixedu.academic.task;
	

import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.messaging.core.domain.MessagingSystem;
import org.fenixedu.messaging.emaildispatch.domain.LocalEmailMessageDispatchReport_Base;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class RemoveUnfinishedReportsTask extends CustomTask {
    @Override public Atomic.TxMode getTxMode() {
        return Atomic.TxMode.READ;
    }


    @Override public void runTask() throws Exception {
        taskLog("before: %s%n", MessagingSystem.getInstance().getUnfinishedReportsSet().size());
        FenixFramework.atomic(() -> {
            MessagingSystem.getInstance().getUnfinishedReportsSet().forEach(report -> {
                try {
                    Method setQueue = LocalEmailMessageDispatchReport_Base.class.getDeclaredMethod("setQueue", MessagingSystem.class);
                    setQueue.setAccessible(true);


                    setQueue.invoke(report, (MessagingSystem) null);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new Error(e);
                }
            });
        });
        taskLog("after: %s%n", MessagingSystem.getInstance().getUnfinishedReportsSet().size());
    }
}