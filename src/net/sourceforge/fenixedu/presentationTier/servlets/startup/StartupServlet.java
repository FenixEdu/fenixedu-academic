package net.sourceforge.fenixedu.presentationTier.servlets.startup;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.sourceforge.fenixedu._development.Custodian;
import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.stm.Transaction;

/**
 * 17/Fev/2003
 * 
 * @author jpvl
 */
public class StartupServlet extends HttpServlet {

    private static final String GRATUITY_SITUATION_CREATOR_TASK_CONFIG = "/GratuitySituationCreatorTask.properties";

    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        Custodian.registerPID();

        super.init(config);

        MetadataManager.init(getServletContext().getRealPath(getInitParameter("domainmodel")));

        SuportePersistenteOJB.fixDescriptors();

        Service.init(RootDomainObject.getInstance());

        Transaction.startMonitoringTransactions();
        
        try {
            try {
                InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils
                        .executeService(null, "ReadCurrentExecutionPeriod", null);
                config.getServletContext().setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY,
                        infoExecutionPeriod);

                setScheduleForGratuitySituationCreation();

            } catch (Throwable e) {
                throw new ServletException("Error reading actual execution period!", e);
            }

            try {
                final Boolean result = (Boolean) ServiceManagerServiceFactory.executeService(null,
                        "CheckIsAliveService", null);

                if (result != null && result.booleanValue()) {
                    System.out.println("Check is alive is working.");
                } else {
                    System.out.println("Check is alive is not working.");
                }
            } catch (Exception ex) {
                System.out.println("Check is alive is not working. Caught excpetion.");
                ex.printStackTrace();
            }

            loadLogins();
        } finally {
            Transaction.forceFinish();
        }
    }

    private void loadLogins() {
        long start = System.currentTimeMillis();
        Login.readLoginByUsername("...PlaceANonExistingLoginHere...");
        long end = System.currentTimeMillis();
        System.out.println("Load of all logins took: " + (end - start));
    }

    private void setScheduleForGratuitySituationCreation() {

        TimerTask gratuitySituationCreatorTask = new TimerTask() {

		public void run() {
		    try {
			try {
			    Object[] args = { "" };
			    ServiceManagerServiceFactory.executeService(null,
									"CreateGratuitySituationsForCurrentExecutionYear",
									args);
			    
			} catch (Exception e) {
			}
			
			// temporary
			try {
			    Object[] args2003_2004 = { "2003/2004" };
			    ServiceManagerServiceFactory.executeService(null,
									"CreateGratuitySituationsForCurrentExecutionYear",
									args2003_2004);
			    
			} catch (Exception e) {
			}
		    } finally {
			Transaction.forceFinish();
		    }
		}
	    };

	Properties properties = new Properties();
	try {
	    properties.load(this.getClass().getResourceAsStream(
								GRATUITY_SITUATION_CREATOR_TASK_CONFIG));

	    Calendar calendar = Calendar.getInstance();
	    String hourString = properties.getProperty("hour");
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

	    timer.schedule(gratuitySituationCreatorTask, firstTimeDate,
			   3600 * 24 * 1000);

	} catch (Exception e) {
	}

    }

}
