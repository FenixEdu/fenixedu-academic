package net.sourceforge.fenixedu.presentationTier.servlets.startup;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
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
import org.fenixedu.bennu.core.presentationTier.servlets.filters.ExceptionHandlerFilter;
import org.fenixedu.bennu.core.presentationTier.servlets.filters.ExceptionHandlerFilter.CustomHandler;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.portal.servlet.PortalBackendRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.plugins.remote.domain.RemoteSystem;

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

        PortalBackendRegistry.registerPortalBackend(new OldCmsPortalBackend());

        logger.info("Fenix initialized successfully");
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
                        && (uri.indexOf("loginExpired.do") == -1) && (uri.indexOf("logoff.do") == -1)
                        && (uri.indexOf("publico/") == -1) && (uri.indexOf("showErrorPage.do") == -1)
                        && (uri.indexOf("showErrorPageRegistered.do") == -1) && (uri.indexOf("exceptionHandlingAction.do") == -1)
                        && (uri.indexOf("siteMap.do") == -1) && (uri.indexOf("fenixEduIndex.do") == -1);
            }

        });
    }

    public static class FenixCustomExceptionHandler implements CustomHandler {
        @Override
        public boolean isCustomizedFor(Throwable t) {
            return true;
        }

        @Override
        public void handle(HttpServletRequest request, ServletResponse response, final Throwable t) throws ServletException,
                IOException {
            ExceptionInformation exceptionInfo = new ExceptionInformation(request, t);

            if (CoreConfiguration.getConfiguration().developmentMode()) {
                request.setAttribute("debugExceptionInfo", exceptionInfo);
            } else {
                request.setAttribute("requestBean", exceptionInfo.getRequestBean());
                request.setAttribute("exceptionInfo", exceptionInfo.getExceptionInfo());
            }

            request.getRequestDispatcher("/showErrorPage.do").forward(request, response);
        }
    }

    private void registerUncaughtExceptionHandler() {
        ExceptionHandlerFilter.register(new FenixCustomExceptionHandler());
    }

}
