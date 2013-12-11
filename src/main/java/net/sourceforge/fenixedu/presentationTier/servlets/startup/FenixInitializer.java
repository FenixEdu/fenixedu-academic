package net.sourceforge.fenixedu.presentationTier.servlets.startup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.CheckIsAliveService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.CreateGratuitySituationsForCurrentExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.DSpaceFileStorage;
import net.sourceforge.fenixedu.domain.Instalation;
import net.sourceforge.fenixedu.domain.PendingRequest;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitNamePart;
import net.sourceforge.fenixedu.domain.person.PersonNamePart;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.PhoneValidationUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.util.ExceptionInformation;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.webServices.jersey.api.FenixJerseyAPIConfig;

import org.apache.commons.fileupload.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.exceptions.BennuCoreDomainException;
import pt.ist.bennu.core.domain.groups.DynamicGroup;
import pt.ist.bennu.core.domain.groups.Group;
import pt.ist.bennu.core.presentationTier.servlets.filters.ExceptionHandlerFilter;
import pt.ist.bennu.core.presentationTier.servlets.filters.ExceptionHandlerFilter.CustomeHandler;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriterFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriterFilter.RequestRewriterFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.plugins.remote.domain.RemoteSystem;
import pt.utl.ist.fenix.tools.file.DSpaceFileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.dspace.DSpaceHttpClient;
import pt.utl.ist.fenix.tools.util.FileUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@WebListener
public class FenixInitializer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(FenixInitializer.class);

    @Override
    @Atomic(mode = TxMode.READ)
    public void contextInitialized(ServletContextEvent event) {

        logger.info("Initializing Fenix");

        Language.setDefaultLocale(Locale.getDefault());

        try {
            final InputStream inputStream = FenixInitializer.class.getResourceAsStream("/build.version");
            PendingRequest.buildVersion = FileUtils.readFile(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new Error("Unable to load build version file");
        }

        RemoteSystem.init();

        try {
            InfoExecutionPeriod infoExecutionPeriod = ReadCurrentExecutionPeriod.run();
            event.getServletContext().setAttribute(PresentationConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);

            setScheduleForGratuitySituationCreation();

        } catch (Throwable e) {
            throw new Error("Error reading actual execution period!", e);
        }

        try {
            final Boolean result = CheckIsAliveService.run();

            if (result != null && result.booleanValue()) {
                logger.info("Check is alive is working.");
            } else {
                logger.info("Check is alive is not working.");
            }
        } catch (Exception ex) {
            logger.info("Check is alive is not working. Caught excpetion.");
            ex.printStackTrace();
        }
        Instalation.ensureInstalation();
        loadLogins();
        loadPersonNames();
        loadUnitNames();
        loadRoles();
        startContactValidationServices();

        registerChecksumFilterRules();
        registerContentInjectionRewriter();
        registerUncaughtExceptionHandler();

        initializeFileManager();
        initializeFenixAPI();
        initializeBennuManagersGroup();

        logger.info("Fenix initialized successfully");
    }

    private static void initializeBennuManagersGroup() {
        try {
            DynamicGroup.getInstance("managers");
        } catch (BennuCoreDomainException e) {
            logger.info("Create managers bennu group to RoleCustomGroup Managers");
            DynamicGroup.initialize("managers", Group.parse("role(MANAGER)"));
        }
    }

    private static void initializeDSpaceFileStorage() {
        DSpaceFileStorage.getInstance();
    }

    private void initializeFenixAPI() {
        FenixJerseyAPIConfig.initialize();
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    private static void initializeFileManager() {
        final Properties properties = new Properties();
        properties.put("dspace.client.transport.class", DSpaceHttpClient.class.getName());
        properties.put("file.manager.factory.implementation.class", DSpaceFileManagerFactory.class.getName());
        properties.put("dspace.serverUrl", FenixConfigurationManager.getConfiguration().getDspaceServerUrl());
        properties.put("dspace.downloadUriFormat", FenixConfigurationManager.getConfiguration().getDspaceDownloadUriFormat());
        properties.put("dspace.username", FenixConfigurationManager.getConfiguration().getDspaceUsername());
        properties.put("dspace.password", FenixConfigurationManager.getConfiguration().getDspacePassword());
        properties.put("dspace.rmi.server.name", FenixConfigurationManager.getConfiguration().getDspaceRMIServerName());
        properties.put("jndi.properties.file", FenixConfigurationManager.getConfiguration().getJNDIPropertiesFile());
        properties.put("rmi.registry.port", FenixConfigurationManager.getConfiguration().getRMIRegistryPort());
        properties.put("rmi.port", FenixConfigurationManager.getConfiguration().getRMIPort());
        properties.put("rmi.ssl", FenixConfigurationManager.getConfiguration().getRMISSL());
        properties.put("rmi.ssl.truststore", FenixConfigurationManager.getConfiguration().getRMISSLTruststore());
        properties.put("rmi.ssl.truststore.password", FenixConfigurationManager.getConfiguration().getRMISSLTruststorePassword());
        properties.put("rmi.stream.bytes.min", FenixConfigurationManager.getConfiguration().getRMIStreamBytesMin());
        properties.put("rmi.stream.bytes.max", FenixConfigurationManager.getConfiguration().getRMIStreamBytesMax());
        properties.put("rmi.stream.bytes.block", FenixConfigurationManager.getConfiguration().getRMIStreamBytesBlock());

        FileManagerFactory.init(DSpaceFileManagerFactory.class.getName());

        DSpaceFileManagerFactory.init(properties);

        initializeDSpaceFileStorage();
    }

    private void startContactValidationServices() {
        PhoneValidationUtils.getInstance();
    }

    private void loadLogins() {
        long start = System.currentTimeMillis();
        User.findByUsername("...PlaceANonExistingLoginHere...");
        long end = System.currentTimeMillis();
        logger.info("Load of all users took: " + (end - start) + "ms.");
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

        start = System.currentTimeMillis();
        for (final UnitName unitName : Bennu.getInstance().getUnitNameSet()) {
            unitName.getName();
        }
        end = System.currentTimeMillis();
        logger.info("Load of all units took: " + (end - start) + "ms.");
    }

    private void loadRoles() {
        long start = System.currentTimeMillis();
        Role.getRoleByRoleType(null);
        long end = System.currentTimeMillis();
        logger.info("Load of all roles took: " + (end - start) + "ms.");
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

    static final int APP_CONTEXT_LENGTH = FenixConfigurationManager.getConfiguration().appContext().length() + 1;

    private void registerChecksumFilterRules() {
        RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
            @Override
            public boolean shouldFilter(HttpServletRequest request) {
                final String uri = request.getRequestURI().substring(APP_CONTEXT_LENGTH);

                if (uri.indexOf("domainbrowser/") >= 0) {
                    return false;
                }
                if (uri.indexOf("images/") >= 0) {
                    return false;
                }
                if (uri.indexOf("gwt/") >= 0) {
                    return false;
                }
                if (uri.indexOf("remote/") >= 0) {
                    return false;
                }
                if (uri.indexOf("javaScript/") >= 0) {
                    return false;
                }
                if (uri.indexOf("script/") >= 0) {
                    return false;
                }
                if (uri.indexOf("ajax/") >= 0) {
                    return false;
                }
                if (uri.indexOf("redirect.do") >= 0) {
                    return false;
                }
                if (uri.indexOf("home.do") >= 0) {
                    return false;
                }
                if (uri.indexOf("/student/fillInquiries.do") >= 0) {
                    return false;
                }
                if (uri.indexOf("/google") >= 0 && uri.endsWith(".html")) {
                    return false;
                }
                if ((uri.indexOf("/teacher/executionCourseForumManagement.do") >= 0 || uri
                        .indexOf("/student/viewExecutionCourseForuns.do") >= 0)
                        && request.getQueryString().indexOf("method=viewThread") >= 0) {
                    return false;
                }
                if (FileUpload.isMultipartContent(request)) {
                    return false;
                }
                final FilterFunctionalityContext filterFunctionalityContext = getContextAttibute(request);
                if (filterFunctionalityContext != null) {
                    final Container container = filterFunctionalityContext.getSelectedTopLevelContainer();
                    if (container != null && container.isPublic() && (uri.indexOf(".do") < 0 || uri.indexOf("publico/") >= 0)) {
                        return false;
                    }
                }
                if (uri.indexOf("notAuthorized.do") >= 0) {
                    return false;
                }

                return uri.length() > 1 && (uri.indexOf("CSS/") == -1) && (uri.indexOf("ajax/") == -1)
                        && (uri.indexOf("images/") == -1) && (uri.indexOf("img/") == -1) && (uri.indexOf("download/") == -1)
                        && (uri.indexOf("external/") == -1) && (uri.indexOf("services/") == -1)
                        && (uri.indexOf("index.jsp") == -1) && (uri.indexOf("index.html") == -1)
                        && (uri.indexOf("login.do") == -1) && (uri.indexOf("loginCAS.do") == -1)
                        && (uri.indexOf("privado") == -1) && (uri.indexOf("loginPage.jsp") == -1)
                        && (uri.indexOf("loginExpired.jsp") == -1) && (uri.indexOf("loginExpired.do") == -1)
                        && (uri.indexOf("logoff.do") == -1) && (uri.indexOf("publico/") == -1)
                        && (uri.indexOf("showErrorPage.do") == -1) && (uri.indexOf("showErrorPageRegistered.do") == -1)
                        && (uri.indexOf("exceptionHandlingAction.do") == -1) && (uri.indexOf("manager/manageCache.do") == -1)
                        && (uri.indexOf("checkPasswordKerberos.do") == -1) && (uri.indexOf("siteMap.do") == -1)
                        && (uri.indexOf("cms/forwardEmailAction.do") == -1) && (uri.indexOf("isAlive.do") == -1)
                        && (uri.indexOf("gwt/") == -1) && (uri.indexOf("remote/") == -1) && (uri.indexOf("downloadFile/") == -1)
                        && !(uri.indexOf("google") >= 0 && uri.endsWith(".html")) && (uri.indexOf("api/fenix") == -1);
            }

            private FilterFunctionalityContext getContextAttibute(final HttpServletRequest httpServletRequest) {
                return (FilterFunctionalityContext) httpServletRequest.getAttribute(FunctionalityContext.CONTEXT_KEY);
            }
        });
    }

    private void registerContentInjectionRewriter() {
        RequestRewriterFilter.registerRequestRewriter(new RequestRewriterFactory() {
            @Override
            public RequestRewriter createRequestRewriter(HttpServletRequest request) {
                return new ContentInjectionRewriter(request);
            }
        });
    }

    public static class FenixCustomExceptionHandler extends CustomeHandler {
        @Override
        public boolean isCustomizedFor(Throwable t) {
            return true;
        }

        @Override
        public void handle(HttpServletRequest request, ServletResponse response, final Throwable t) throws ServletException,
                IOException {

            if (request.getAttribute("requestBean") == null) {

                SupportRequestBean requestBean = SupportRequestBean.generateExceptionBean(AccessControl.getPerson());

                if (AbstractFunctionalityContext.getCurrentContext(request) != null) {
                    requestBean.setRequestContext(AbstractFunctionalityContext.getCurrentContext(request)
                            .getSelectedTopLevelContainer());
                }
                request.setAttribute("requestBean", requestBean);
                request.setAttribute("exceptionInfo", ExceptionInformation.buildUncaughtExceptionInfo(request, t));
            }

            t.printStackTrace();

            String urlToForward =
                    t instanceof IllegalDataAccessException ? "/exception/notAuthorizedForward.jsp" : "/showErrorPage.do";
            request.getRequestDispatcher(urlToForward).forward(request, response);
        }
    }

    private void registerUncaughtExceptionHandler() {
        ExceptionHandlerFilter.register(new FenixCustomExceptionHandler());
    }

}
