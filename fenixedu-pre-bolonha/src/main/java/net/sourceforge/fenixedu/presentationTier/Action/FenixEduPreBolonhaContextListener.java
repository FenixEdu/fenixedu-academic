package org.fenixedu.academic.ui.struts.action;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.fenixedu.academic.service.services.masterDegree.administrativeOffice.gratuity.CreateGratuitySituationsForCurrentExecutionYear;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.FenixEduAcademicConfiguration;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.FenixFramework;

@WebListener
public class FenixEduPreBolonhaContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FenixFramework.getDomainModel().registerDeletionBlockerListener(ExecutionDegree.class, (executionDegree, blockers) -> {
            if (executionDegree.getMasterDegreeCandidatesSet().isEmpty()) {
                blockers.add(BundleUtil.getString(Bundle.APPLICATION, "execution.degree.cannot.be.deleted"));
            }
        });
        FenixFramework.getDomainModel().registerDeletionBlockerListener(Person.class, (person, blockers) -> {
            if (person.getMasterDegreeCandidatesSet().isEmpty()) {
                blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.person.cannot.be.deleted"));
            }
        });
        FenixFramework.getDomainModel().registerDeletionListener(StudentCurricularPlan.class, (studentCurricularPlan) -> {
            studentCurricularPlan.setMasterDegreeThesis(null);
        });

        try {
            setScheduleForGratuitySituationCreation();
        } catch (Throwable e) {
            throw new Error("Error reading actual execution period!", e);
        }

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

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
