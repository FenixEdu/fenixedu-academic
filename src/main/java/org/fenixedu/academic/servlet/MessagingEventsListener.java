package org.fenixedu.academic.servlet;

import org.fenixedu.academic.domain.PersonInformationLog;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.MessagingConfiguration;
import org.fenixedu.bennu.core.signals.Signal;
import org.fenixedu.messaging.core.ui.OptInUpdateEvent;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MessagingEventsListener implements ServletContextListener {

    @Override public void contextInitialized(ServletContextEvent sce) {
        Signal.register(MessagingConfiguration.OPTIN_STATUS_UPDATE,
                (OptInUpdateEvent event) -> PersonInformationLog.createLog(event.getUser().getPerson(), Bundle.MESSAGING,
                        event.isNewOptInStatus() ? "opt.in.update.add" : "opt.in.update.remove", event.getUser().getUsername(),
                        event.getSender().getName()));
    }

    @Override public void contextDestroyed(ServletContextEvent sce) {

    }
}
