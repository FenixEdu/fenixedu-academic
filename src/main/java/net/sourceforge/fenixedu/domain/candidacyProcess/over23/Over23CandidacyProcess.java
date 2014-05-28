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
package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessSelectDegreesBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyProcessCandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.Over23CandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

import com.google.common.collect.Sets;

public class Over23CandidacyProcess extends Over23CandidacyProcess_Base {

    static {
        getRelationCandidacyPeriodCandidacyProcess().addListener(
                new RelationAdapter<CandidacyProcess, CandidacyProcessCandidacyPeriod>() {
                    @Override
                    public void beforeAdd(CandidacyProcess candidacyProcess, CandidacyProcessCandidacyPeriod candidacyPeriod) {
                        super.beforeAdd(candidacyProcess, candidacyPeriod);

                        if (candidacyProcess != null && candidacyPeriod != null
                                && candidacyPeriod instanceof Over23CandidacyPeriod) {
                            if (candidacyPeriod.hasAnyCandidacyProcesses()) {
                                throw new DomainException("error.Over23CandidacyProcess.candidacy.period.already.has.process");
                            }
                        }
                    }
                });
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new EditCandidacyPeriod());
        activities.add(new SendInformationToJury());
        activities.add(new PrintCandidacies());
        activities.add(new SelectAvailableDegrees());
    }

    private Over23CandidacyProcess() {
        super();
    }

    private Over23CandidacyProcess(final ExecutionYear executionYear, final DateTime start, final DateTime end) {
        this();
        checkParameters(executionYear, start, end);
        setState(CandidacyProcessState.STAND_BY);
        new Over23CandidacyPeriod(this, executionYear, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
        if (executionInterval == null) {
            throw new DomainException("error.Over23CandidacyProcess.invalid.executionInterval");
        }

        if (start == null || end == null || start.isAfter(end)) {
            throw new DomainException("error.Over23CandidacyProcess.invalid.interval");
        }
    }

    private void edit(final DateTime start, final DateTime end) {
        checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
        getCandidacyPeriod().edit(start, end);
    }

    @Override
    public boolean canExecuteActivity(User userView) {
        return isAllowedToManageProcess(userView) || userView.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL);
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    public List<Over23IndividualCandidacyProcess> getOver23IndividualCandidaciesThatCanBeSendToJury() {
        final List<Over23IndividualCandidacyProcess> result = new ArrayList<Over23IndividualCandidacyProcess>();
        for (final IndividualCandidacyProcess child : getChildProcesses()) {
            final Over23IndividualCandidacyProcess over23CP = (Over23IndividualCandidacyProcess) child;
            if (over23CP.isCandidacyValid()) {
                result.add(over23CP);
            }
        }
        return result;
    }

    public List<Over23IndividualCandidacyProcess> getAcceptedOver23IndividualCandidacies() {
        final List<Over23IndividualCandidacyProcess> result = new ArrayList<Over23IndividualCandidacyProcess>();
        for (final IndividualCandidacyProcess child : getChildProcesses()) {
            if (child.isCandidacyValid() && child.isCandidacyAccepted()) {
                result.add((Over23IndividualCandidacyProcess) child);
            }
        }
        return result;
    }

    // static information

    private static final Set<DegreeType> ALLOWED_DEGREE_TYPES = Sets.newHashSet(DegreeType.BOLONHA_DEGREE,
            DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);

    static private boolean isAllowedToManageProcess(User userView) {
        for (AcademicProgram program : AcademicAuthorizationGroup.getProgramsForOperation(userView.getPerson(), AcademicOperationType.MANAGE_CANDIDACY_PROCESSES)) {
            if (ALLOWED_DEGREE_TYPES.contains(program.getDegreeType())) {
                return true;
            }
        }
        return false;
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<Over23CandidacyProcess> {

        @Override
        public void checkPreConditions(Over23CandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, User userView, Object object) {
            final CandidacyProcessBean bean = (CandidacyProcessBean) object;
            return new Over23CandidacyProcess((ExecutionYear) bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
        }
    }

    static private class EditCandidacyPeriod extends Activity<Over23CandidacyProcess> {

        @Override
        public void checkPreConditions(Over23CandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, User userView, Object object) {
            final CandidacyProcessBean bean = (CandidacyProcessBean) object;
            process.edit(bean.getStart(), bean.getEnd());
            return process;
        }
    }

    static private class SendInformationToJury extends Activity<Over23CandidacyProcess> {

        @Override
        public void checkPreConditions(Over23CandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isInStandBy()) {
                throw new PreConditionNotValidException();
            }

            if (!process.hasCandidacyPeriod() || !process.hasStarted() || process.hasOpenCandidacyPeriod()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, User userView, Object object) {
            process.setState(CandidacyProcessState.SENT_TO_JURY);
            return process;
        }
    }

    static private class PrintCandidacies extends Activity<Over23CandidacyProcess> {

        @Override
        public void checkPreConditions(Over23CandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, User userView, Object object) {
            return process; // for now, nothing to be done
        }
    }

    static private class SelectAvailableDegrees extends Activity<Over23CandidacyProcess> {

        @Override
        public void checkPreConditions(Over23CandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, User userView, Object object) {
            final CandidacyProcessSelectDegreesBean bean = (CandidacyProcessSelectDegreesBean) object;
            final List<Degree> degrees = bean.getDegrees();
            process.getDegreeSet().addAll(degrees);
            process.getDegreeSet().retainAll(degrees);
            return process;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return Boolean.FALSE;
        }

    }

}
