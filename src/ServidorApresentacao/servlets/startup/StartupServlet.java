package ServidorApresentacao.servlets.startup;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import DataBeans.InfoExecutionPeriod;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

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
        super.init(config);
        try {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(
                    null, "ReadCurrentExecutionPeriod", null);
            config.getServletContext().setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY,
                    infoExecutionPeriod);

            setScheduleForGratuitySituationCreation();

        } catch (Throwable e) {
            e.printStackTrace(System.out);
            throw new ServletException("Error reading actual execution period!", e);
        }
    }

    /**
     *  
     */
    private void setScheduleForGratuitySituationCreation() {

        TimerTask gratuitySituationCreatorTask = new TimerTask() {

            public void run() {
                Object[] args = {};

                try {
                    ServiceManagerServiceFactory.executeService(null,
                            "CreateGratuitySituationsForCurrentExecutionYear", args);

                } catch (Exception e) {
                    //TODO: This should be written in log file
                    System.out.println("Gratuity Situation Creator task failed to execute");
                    e.printStackTrace();
                }

            }

        };

        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream(GRATUITY_SITUATION_CREATOR_TASK_CONFIG));

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

            //System.out.println("Timer scheduled for: " +
            // firstTimeDate.toString());

            Timer timer = new Timer();

            timer.schedule(gratuitySituationCreatorTask, firstTimeDate, 3600 * 24 * 1000);

        } catch (Exception e) {
            //TODO: This should be written in log file
            System.out.println("Error loading Gratuity Situation Creator Task config file");
            e.printStackTrace();
        }

    }
}