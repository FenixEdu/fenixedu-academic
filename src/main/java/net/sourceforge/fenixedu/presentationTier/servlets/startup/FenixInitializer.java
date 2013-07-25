package net.sourceforge.fenixedu.presentationTier.servlets.startup;

import pt.ist.fenixWebFramework.FenixWebFramework;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.plugins.remote.domain.RemoteSystem;
import pt.ist.fenixframework.plugins.scheduler.Scheduler;
import pt.ist.fenixframework.plugins.scheduler.domain.SchedulerSystem;

import pt.utl.ist.fenix.tools.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.applicationTier.Servico.CheckIsAliveService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.content.CreateMetaDomainObectTypes;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.CreateGratuitySituationsForCurrentExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.PendingRequest;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitNamePart;
import net.sourceforge.fenixedu.domain.person.PersonNamePart;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.PhoneValidationUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class FenixInitializer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(FenixInitializer.class);

    @Override
    @Atomic(mode = TxMode.READ)
    public void contextInitialized(ServletContextEvent event) {

        logger.info("Initializing Fenix");

        try {
            final InputStream inputStream = Authenticate.class.getResourceAsStream("/build.version");
            PendingRequest.buildVersion = FileUtils.readFile(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new Error("Unable to load build version file");
        }

        RootDomainObject.ensureRootDomainObject();
        RootDomainObject.initialize();
        RemoteSystem.init();

        try {
            InfoExecutionPeriod infoExecutionPeriod = ReadCurrentExecutionPeriod.run();
            event.getServletContext().setAttribute(PresentationConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);

            setScheduleForGratuitySituationCreation();

        } catch (Throwable e) {
            throw new Error("Error reading actual execution period!", e);
        }

        try {
            long start = System.currentTimeMillis();
            CreateMetaDomainObectTypes.run();
            long end = System.currentTimeMillis();
            logger.info("CreateMetaDomainObectTypes: " + (end - start) + "ms.");
        } catch (Throwable throwable) {
            throw new Error("Error creating MetaDomainObject!", throwable);
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
        loadLogins();
        loadPersonNames();
        loadUnitNames();
        loadRoles();
        startContactValidationServices();
        logger.info("Fenix initialized successfully");
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    private void startContactValidationServices() {
        PhoneValidationUtils.getInstance();
    }

    private void loadLogins() {
        long start = System.currentTimeMillis();
        Login.readLoginByUsername("...PlaceANonExistingLoginHere...");
        long end = System.currentTimeMillis();
        logger.info("Load of all logins took: " + (end - start) + "ms.");
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
        for (final UnitName unitName : RootDomainObject.getInstance().getUnitNameSet()) {
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
            String hourString = PropertiesManager.getProperty("gratuity.situation.creator.task.hour");
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

}
