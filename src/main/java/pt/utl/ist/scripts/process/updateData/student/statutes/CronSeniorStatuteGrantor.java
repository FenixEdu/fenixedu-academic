package pt.utl.ist.scripts.process.updateData.student.statutes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.LocalDate;
import org.slf4j.Logger;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Task(englishTitle = "CronSeniorStatuteGrantor")
public class CronSeniorStatuteGrantor extends CronTask {

    final static int HOW_MANY_WEEKS_SOONER = 3;

    private long cntBSc = 0;
    private long cntMSc = 0;
    private long cntIM = 0;
    private long cntTotal = 0;

    @Override
    public void runTask() {
        getLogger().info(".: Checking if a special season enrolment period is coming soon... :.");
        ExecutionYear subjectYear = specialSeasonEnrolmentPeriodOpeningSoonForThisYear(HOW_MANY_WEEKS_SOONER);

        if (subjectYear != null) {
            getLogger().info("   --> HEADSHOT! Running massive SeniorStatute grantor now.");

            try {
                massivelyGrantTheFingSeniorStatute(subjectYear);
            } catch (InterruptedException e) {
                throw new Error(e);
            }

            getLogger().info("\n       Number of Senior statute grantings  (BSc): " + cntBSc + ".");
            getLogger().info("       Number of Senior statute grantings (MSc): " + cntMSc + ".");
            getLogger().info("       Number of Senior statute grantings (Int Msc): " + cntIM + ".");
            getLogger().info("       Total number of Senior statute grantings: " + cntTotal + ".\n\n");

            getLogger().info("\n\n·: That's all for today folks!!                                  :·");
        } else {
            getLogger().info("·: Nothing happening today. Over and out.                        :·");
        }

    }

    protected ExecutionYear specialSeasonEnrolmentPeriodOpeningSoonForThisYear(Integer howSoon) {
        Set<EnrolmentPeriod> enrolmentPeriods = Bennu.getInstance().getEnrolmentPeriodsSet();
        Map<DegreeType, ExecutionSemester> fallTermEnrolmentPeriods = new HashMap<DegreeType, ExecutionSemester>();
        Map<DegreeType, ExecutionSemester> springTermEnrolmentPeriods = new HashMap<DegreeType, ExecutionSemester>();
        Map<DegreeType, ExecutionSemester> switcherTermEnrolmentPeriods = null;

        for (EnrolmentPeriod enrolmentPeriod : enrolmentPeriods) {
            if (!(enrolmentPeriod instanceof EnrolmentPeriodInSpecialSeasonEvaluations)) {
                continue;
            }
            LocalDate statuteGrantorStartDate = new LocalDate(enrolmentPeriod.getStartDateDateTime().toLocalDate());
            statuteGrantorStartDate = statuteGrantorStartDate.minusWeeks(howSoon);
            LocalDate statuteGrantorStopDate = new LocalDate(enrolmentPeriod.getEndDateDateTime().toLocalDate());
            statuteGrantorStopDate = statuteGrantorStopDate.plusDays(1); //inc 1 so that today is compared as before or equal to enrolmentPeriod end date
            LocalDate today = new LocalDate();
            if (today.isAfter(statuteGrantorStartDate) && today.isBefore(statuteGrantorStopDate)) {

                if (enrolmentPeriod.getExecutionPeriod().isFirstOfYear()) {
                    switcherTermEnrolmentPeriods = fallTermEnrolmentPeriods;
                } else {
                    switcherTermEnrolmentPeriods = springTermEnrolmentPeriods;
                }

                if (enrolmentPeriod.getDegree().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
                    if (switcherTermEnrolmentPeriods.get(DegreeType.BOLONHA_DEGREE) == null) {
                        switcherTermEnrolmentPeriods.put(DegreeType.BOLONHA_DEGREE, enrolmentPeriod.getExecutionPeriod());
                    }
                }

                if (enrolmentPeriod.getDegree().getDegreeType() == DegreeType.BOLONHA_MASTER_DEGREE) {
                    if (switcherTermEnrolmentPeriods.get(DegreeType.BOLONHA_MASTER_DEGREE) == null) {
                        switcherTermEnrolmentPeriods.put(DegreeType.BOLONHA_MASTER_DEGREE, enrolmentPeriod.getExecutionPeriod());
                    }
                }

                if (enrolmentPeriod.getDegree().getDegreeType() == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
                    if (switcherTermEnrolmentPeriods.get(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) == null) {
                        switcherTermEnrolmentPeriods.put(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE,
                                enrolmentPeriod.getExecutionPeriod());
                    }
                }
            }
        }

        if (fallTermEnrolmentPeriods.get(DegreeType.BOLONHA_DEGREE) == null) {
            return null;
        }
        if (fallTermEnrolmentPeriods.get(DegreeType.BOLONHA_MASTER_DEGREE) == null) {
            return null;
        }
        if (fallTermEnrolmentPeriods.get(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) == null) {
            return null;
        }

        if (springTermEnrolmentPeriods.get(DegreeType.BOLONHA_DEGREE) == null) {
            return null;
        }
        if (springTermEnrolmentPeriods.get(DegreeType.BOLONHA_MASTER_DEGREE) == null) {
            return null;
        }
        if (springTermEnrolmentPeriods.get(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) == null) {
            return null;
        }

        if (!(fallTermEnrolmentPeriods.get(DegreeType.BOLONHA_DEGREE).getExecutionYear() == springTermEnrolmentPeriods.get(
                DegreeType.BOLONHA_DEGREE).getExecutionYear())) {
            return null;
        }

        if (!(fallTermEnrolmentPeriods.get(DegreeType.BOLONHA_MASTER_DEGREE).getExecutionYear() == springTermEnrolmentPeriods
                .get(DegreeType.BOLONHA_MASTER_DEGREE).getExecutionYear())) {
            return null;
        }

        if (!(fallTermEnrolmentPeriods.get(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE).getExecutionYear() == springTermEnrolmentPeriods
                .get(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE).getExecutionYear())) {
            return null;
        }

        if (!(fallTermEnrolmentPeriods.get(DegreeType.BOLONHA_DEGREE) == fallTermEnrolmentPeriods
                .get(DegreeType.BOLONHA_MASTER_DEGREE))) {
            return null;
        }

        if (!(fallTermEnrolmentPeriods.get(DegreeType.BOLONHA_DEGREE) == fallTermEnrolmentPeriods
                .get(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE))) {
            return null;
        }

        return fallTermEnrolmentPeriods.get(DegreeType.BOLONHA_DEGREE).getExecutionYear();
    }

    protected void massivelyGrantTheFingSeniorStatute(ExecutionYear executionYear) throws InterruptedException {

        for (Registration registration : generateRegistrationSet(executionYear)) {
            if (registration.isSeniorStatuteApplicable(executionYear)) {

                final GrantSeniorStatute grantor = new GrantSeniorStatute(registration, executionYear, getLogger());
                grantor.start();
                grantor.join();

                if (registration.getDegreeType() == DegreeType.BOLONHA_DEGREE) {
                    cntBSc++;
                } else if (registration.getDegreeType() == DegreeType.BOLONHA_MASTER_DEGREE) {
                    cntMSc++;
                } else if (registration.getDegreeType() == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
                    cntIM++;
                }
                cntTotal++;
            }
        }

    }

    private List<Registration> generateRegistrationSet(ExecutionYear executionYear) {
        List<Registration> registrations = new ArrayList<Registration>();

        // Triage for proper DCPs
        for (DegreeCurricularPlan dcp : Bennu.getInstance().getDegreeCurricularPlansSet()) {
            if (dcp.isBolonhaDegree()) {
                // Triage for the right students
                for (Registration registration : dcp.getActiveRegistrations()) {
                    if (registration.hasAnyEnrolmentsIn(executionYear)) {
                        if (registration.isEnrolmentByStudentAllowed()) {
                            registrations.add(registration);
                        }
                    }
                }
            }
        }
        return registrations;
    }

    private static class GrantSeniorStatute extends Thread {
        private final Registration registration;
        private final ExecutionYear executionYear;
        private Logger log;

        protected GrantSeniorStatute(final Registration registration, final ExecutionYear executionYear, final Logger log) {
            this.registration = registration;
            this.executionYear = executionYear;
            this.log = log;
        }

        private Registration getRegistration() {
            return registration;
        }

        private ExecutionYear getExecutionYear() {
            return executionYear;
        }

        @Atomic
        @Override
        public void run() {
            Language.setLocale(new Locale("pt", "PT"));
            try {
                getRegistration().grantSeniorStatute(getExecutionYear());
            } catch (final Exception e) {
                log.error("Error while granting SeniorStatute to '" + getRegistration().getPerson().getName()
                        + "' for his/her registration in <" + getRegistration().getDegreeNameWithDescription() + ">.", e);
                throw new Error(e);
            }
        }
    }

}
