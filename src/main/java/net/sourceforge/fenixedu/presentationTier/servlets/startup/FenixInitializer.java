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
package net.sourceforge.fenixedu.presentationTier.servlets.startup;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Session;
import javax.mail.Transport;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.CreateGratuitySituationsForCurrentExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Installation;
import net.sourceforge.fenixedu.domain.cms.OldCmsPortalBackend;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitNamePart;
import net.sourceforge.fenixedu.domain.person.PersonNamePart;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.PhoneValidationUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.util.ExceptionInformation;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.webServices.jersey.api.FenixJerseyAPIConfig;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.User.UserPresentationStrategy;
import org.fenixedu.bennu.core.rest.Healthcheck;
import org.fenixedu.bennu.core.rest.SystemResource;
import org.fenixedu.bennu.core.servlets.ExceptionHandlerFilter;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.portal.servlet.PortalBackendRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.plugins.remote.domain.RemoteSystem;

import com.sun.mail.smtp.SMTPTransport;

@WebListener
public class FenixInitializer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(FenixInitializer.class);

    @Override
    @Atomic(mode = TxMode.READ)
    public void contextInitialized(ServletContextEvent event) {

        logger.info("Initializing Fenix");

        RemoteSystem.init();

        try {
            InfoExecutionPeriod infoExecutionPeriod = ReadCurrentExecutionPeriod.run();
            event.getServletContext().setAttribute(PresentationConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);

            setScheduleForGratuitySituationCreation();

        } catch (Throwable e) {
            throw new Error("Error reading actual execution period!", e);
        }

        Installation.ensureInstallation();
        loadPersonNames();
        loadUnitNames();
        startContactValidationServices();

        registerChecksumFilterRules();
        registerUncaughtExceptionHandler();

        initializeFenixAPI();
        registerPresentationStrategy();

        registerHealthchecks();

        PortalBackendRegistry.registerPortalBackend(new OldCmsPortalBackend());

        logger.info("Fenix initialized successfully");
    }

    private void registerHealthchecks() {
        SystemResource.registerHealthcheck(new Healthcheck() {
            @Override
            public String getName() {
                return "SMTP";
            }

            @Override
            protected Result check() throws Exception {
                final Properties properties = new Properties();
                properties.put("mail.transport.protocol", "smtp");
                Transport transport = Session.getInstance(properties).getTransport();
                transport.connect(FenixConfigurationManager.getConfiguration().getMailSmtpHost(), null, null);
                String response = ((SMTPTransport) transport).getLastServerResponse();
                transport.close();
                return Result.healthy("SMTP server returned response: " + response);
            }
        });
    }

    private void registerPresentationStrategy() {
        User.registerUserPresentationStrategy(new UserPresentationStrategy() {
            @Override
            public String shortPresent(User user) {
                if (user.getPerson() != null) {
                    return user.getPerson().getNickname();
                } else {
                    return user.getUsername();
                }
            }

            @Override
            public String present(User user) {
                if (user.getPerson() != null) {
                    return user.getPerson().getNickname();
                } else {
                    return user.getUsername();
                }
            }
        });
    }

    private void initializeFenixAPI() {
        FenixJerseyAPIConfig.initialize();
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    private void startContactValidationServices() {
        PhoneValidationUtils.getInstance();
    }

    private void loadPersonNames() {
        long start = System.currentTimeMillis();
        PersonNamePart.find("...PlaceANonExistingPersonNameHere...");
        long end = System.currentTimeMillis();
        logger.info("Load of all person names took: " + (end - start) + "ms.");
    }

    private void loadUnitNames() {
        long start = System.currentTimeMillis();
        UnitNamePart.find("...PlaceANonExistingUnitNameHere...");
        long end = System.currentTimeMillis();
        logger.info("Load of all unit names took: " + (end - start) + "ms.");
    }

    private void setScheduleForGratuitySituationCreation() {

        TimerTask gratuitySituationCreatorTask = new TimerTask() {

            @Override
            public void run() {
                try {
                    CreateGratuitySituationsForCurrentExecutionYear.runCreateGratuitySituationsForCurrentExecutionYear("");
                } catch (Exception e) {
                }

                // temporary
                try {
                    CreateGratuitySituationsForCurrentExecutionYear
                            .runCreateGratuitySituationsForCurrentExecutionYear("2003/2004");
                } catch (Exception e) {
                }
            }
        };

        try {
            Calendar calendar = Calendar.getInstance();
            String hourString = FenixConfigurationManager.getConfiguration().getGratuitySituationCreatorTaskHour();
            int scheduledHour = Integer.parseInt(hourString);
            if (scheduledHour == -1) {
                return;
            }
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

            calendar.set(Calendar.HOUR_OF_DAY, scheduledHour);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (currentHour >= scheduledHour) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            Date firstTimeDate = calendar.getTime();

            Timer timer = new Timer();

            timer.schedule(gratuitySituationCreatorTask, firstTimeDate, 3600 * 24 * 1000);

        } catch (Exception e) {
        }

    }

    private void registerChecksumFilterRules() {
        RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
            @Override
            public boolean shouldFilter(HttpServletRequest request) {
                final String uri = request.getRequestURI().substring(request.getContextPath().length());
                if (uri.indexOf("redirect.do") >= 0) {
                    return false;
                }
                if (uri.indexOf("home.do") >= 0) {
                    return false;
                }
                if (uri.indexOf("/student/fillInquiries.do") >= 0) {
                    return false;
                }
                if ((uri.indexOf("/teacher/executionCourseForumManagement.do") >= 0 || uri
                        .indexOf("/student/viewExecutionCourseForuns.do") >= 0)
                        && request.getQueryString().indexOf("method=viewThread") >= 0) {
                    return false;
                }
                if (ServletFileUpload.isMultipartContent(request)) {
                    return false;
                }
                if (uri.indexOf("notAuthorized.do") >= 0) {
                    return false;
                }

                return (uri.indexOf("external/") == -1) && (uri.indexOf("login.do") == -1) && (uri.indexOf("loginCAS.do") == -1)
                        && (uri.indexOf("logoff.do") == -1) && (uri.indexOf("publico/") == -1)
                        && (uri.indexOf("showErrorPage.do") == -1) && (uri.indexOf("showErrorPageRegistered.do") == -1)
                        && (uri.indexOf("exceptionHandlingAction.do") == -1) && (uri.indexOf("siteMap.do") == -1)
                        && (uri.indexOf("fenixEduIndex.do") == -1);
            }

        });
    }

    private void registerUncaughtExceptionHandler() {
        ExceptionHandlerFilter.setExceptionHandler((request, response, t) -> {
            if (response.isCommitted()) {
                // We cannot forward to the error form after the response is committed
                return false;
            }

            HttpServletRequest req = (HttpServletRequest) request;

            ExceptionInformation exceptionInfo = new ExceptionInformation(req, t);

            logger.error("Request at " + req.getRequestURI() + " threw an exception: ", t);

            if (CoreConfiguration.getConfiguration().developmentMode()) {
                request.setAttribute("debugExceptionInfo", exceptionInfo);
            } else {
                request.setAttribute("requestBean", exceptionInfo.getRequestBean());
                request.setAttribute("exceptionInfo", exceptionInfo.getExceptionInfo());
            }

            request.getRequestDispatcher("/showErrorPage.do").forward(request, response);
            return true;
        });
    }

}
