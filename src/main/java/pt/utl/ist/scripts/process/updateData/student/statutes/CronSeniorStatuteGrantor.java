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
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.LocalDate;

@Task(englishTitle = "CronSeniorStatuteGrantor", readOnly = true)
public class CronSeniorStatuteGrantor extends CronTask {

    final static int HOW_MANY_WEEKS_SOONER = 3;

    private long cntBSc = 0;
    private long cntMSc = 0;
    private long cntIM = 0;
    private long cntTotal = 0;

    @Override
    public void runTask() {
        I18N.setLocale(new Locale("pt", "PT"));
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
                try {
                    registration.grantSeniorStatute(executionYear);
                } catch (final Exception e) {
                    getLogger().error(
                            "Error while granting SeniorStatute to '" + registration.getPerson().getName()
                                    + "' for his/her registration in <" + registration.getDegreeNameWithDescription() + ">.", e);
                    throw new Error(e);
                }

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
}
