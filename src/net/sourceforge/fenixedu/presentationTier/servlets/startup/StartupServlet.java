package net.sourceforge.fenixedu.presentationTier.servlets.startup;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.applicationTier.Servico.CheckIsAliveService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.content.CreateMetaDomainObectTypes;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.PendingRequest;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitNamePart;
import net.sourceforge.fenixedu.domain.person.PersonNamePart;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFrameworkInitializer;
import pt.ist.fenixframework.plugins.scheduler.domain.SchedulerSystem;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.util.FileUtils;

/**
 * 17/Fev/2003
 * 
 * @author jpvl
 */
public class StartupServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
	// Custodian.registerPID();

	super.init(config);

	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(FenixFrameworkInitializer.CONFIG_PATH));

	try {
	    final InputStream inputStream = Authenticate.class.getResourceAsStream("/.build.version");
	    PendingRequest.buildVersion = FileUtils.readFile(inputStream);
	} catch (java.io.IOException e) {
	    throw new ServletException("Unable to load build version file");
	}

	FenixService.init(RootDomainObject.getInstance());

	try {
	    try {
		InfoExecutionPeriod infoExecutionPeriod = ReadCurrentExecutionPeriod.run();
		config.getServletContext().setAttribute(PresentationConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);

		setScheduleForGratuitySituationCreation();

	    } catch (Throwable e) {
		throw new ServletException("Error reading actual execution period!", e);
	    }

	    try {
		long start = System.currentTimeMillis();
		CreateMetaDomainObectTypes.run();
		long end = System.currentTimeMillis();
		System.out.println("CreateMetaDomainObectTypes: " + (end - start) + "ms.");
	    } catch (Throwable throwable) {
		throw new ServletException("Error creating MetaDomainObject!", throwable);
	    }

	    try {
		final Boolean result = CheckIsAliveService.run();

		if (result != null && result.booleanValue()) {
		    System.out.println("Check is alive is working.");
		} else {
		    System.out.println("Check is alive is not working.");
		}
	    } catch (Exception ex) {
		System.out.println("Check is alive is not working. Caught excpetion.");
		ex.printStackTrace();
	    }
	    FenixReport.setRealPath(getServletContext().getRealPath("/"));

	    loadLogins();
	    loadPersonNames();
	    loadUnitNames();
	    loadRoles();

	    clearAllScheduledTasks();
	} finally {
	    Transaction.forceFinish();
	}
    }

    @Service
    private void clearAllScheduledTasks() {
	final String scheduleSystemFlag = PropertiesManager.getProperty("schedule.system");
	if (scheduleSystemFlag == null || scheduleSystemFlag.isEmpty() || !scheduleSystemFlag.equalsIgnoreCase("active")) {
	    SchedulerSystem.getInstance().clearAllScheduledTasks();
	}
    }

    private void loadLogins() {
	long start = System.currentTimeMillis();
	Login.readLoginByUsername("...PlaceANonExistingLoginHere...");
	long end = System.currentTimeMillis();
	System.out.println("Load of all logins took: " + (end - start) + "ms.");
    }

    private void loadPersonNames() {
	long start = System.currentTimeMillis();
	PersonNamePart.find("...PlaceANonExistingPersonNameHere...");
	long end = System.currentTimeMillis();
	System.out.println("Load of all person names took: " + (end - start) + "ms.");
    }

    private void loadUnitNames() {
	long start = System.currentTimeMillis();
	UnitNamePart.find("...PlaceANonExistingUnitNameHere...");
	long end = System.currentTimeMillis();
	System.out.println("Load of all unit names took: " + (end - start) + "ms.");

	start = System.currentTimeMillis();
	for (final UnitName unitName : RootDomainObject.getInstance().getUnitNameSet()) {
	    unitName.getName();
	}
	end = System.currentTimeMillis();
	System.out.println("Load of all units took: " + (end - start) + "ms.");
    }

    private void loadRoles() {
	long start = System.currentTimeMillis();
	Role.getRoleByRoleType(null);
	long end = System.currentTimeMillis();
	System.out.println("Load of all roles took: " + (end - start) + "ms.");
    }

    private void setScheduleForGratuitySituationCreation() {

	TimerTask gratuitySituationCreatorTask = new TimerTask() {

	    @Override
	    public void run() {
		try {
		    try {
			Object[] args = { "" };
			ServiceManagerServiceFactory.executeService("CreateGratuitySituationsForCurrentExecutionYear", args);

		    } catch (Exception e) {
		    }

		    // temporary
		    try {
			Object[] args2003_2004 = { "2003/2004" };
			ServiceManagerServiceFactory.executeService("CreateGratuitySituationsForCurrentExecutionYear",
				args2003_2004);

		    } catch (Exception e) {
		    }
		} finally {
		    Transaction.forceFinish();
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