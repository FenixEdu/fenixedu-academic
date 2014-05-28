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
package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessSelectDegreesBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.SecondCycleCandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import com.google.common.collect.Sets;

public class SecondCycleCandidacyProcess extends SecondCycleCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new EditCandidacyPeriod());
        activities.add(new SendToCoordinator());
        activities.add(new PrintCandidacies());
        activities.add(new ExportCandidacies());
        activities.add(new SendToScientificCouncil());
        activities.add(new ViewChildProcessWithMissingRequiredDocumentFiles());
        activities.add(new SelectAvailableDegrees());
    }

    private SecondCycleCandidacyProcess() {
        super();
    }

    private SecondCycleCandidacyProcess(final ExecutionYear executionYear, final DateTime start, final DateTime end) {
        this();
        checkParameters(executionYear, start, end);
        setState(CandidacyProcessState.STAND_BY);
        new SecondCycleCandidacyPeriod(this, executionYear, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
        if (executionInterval == null) {
            throw new DomainException("error.SecondCycleCandidacyProcess.invalid.executionInterval");
        }

        if (start == null || end == null || start.isAfter(end)) {
            throw new DomainException("error.SecondCycleCandidacyProcess.invalid.interval");
        }
    }

    private void edit(final DateTime start, final DateTime end) {
        checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
        getCandidacyPeriod().edit(start, end);
    }

    @Override
    public boolean canExecuteActivity(User userView) {
        return isAllowedToManageProcess(userView) || userView.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)
                || userView.getPerson().hasRole(RoleType.COORDINATOR);
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
        return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    public List<SecondCycleIndividualCandidacyProcess> getValidSecondCycleIndividualCandidacies() {
        final List<SecondCycleIndividualCandidacyProcess> result = new ArrayList<SecondCycleIndividualCandidacyProcess>();
        for (final IndividualCandidacyProcess child : getChildProcesses()) {
            final SecondCycleIndividualCandidacyProcess process = (SecondCycleIndividualCandidacyProcess) child;
            if (process.isCandidacyValid()) {
                result.add(process);
            }
        }
        return result;
    }

    public List<SecondCycleIndividualCandidacyProcess> getValidSecondCycleIndividualCandidacies(final Degree degree) {
        if (degree == null) {
            return Collections.emptyList();
        }
        final List<SecondCycleIndividualCandidacyProcess> result = new ArrayList<SecondCycleIndividualCandidacyProcess>();
        for (final IndividualCandidacyProcess child : getChildProcesses()) {
            final SecondCycleIndividualCandidacyProcess process = (SecondCycleIndividualCandidacyProcess) child;
            if (process.isCandidacyValid() && process.hasCandidacyForSelectedDegree(degree)) {
                result.add(process);
            }
        }
        return result;
    }

    public Map<Degree, SortedSet<SecondCycleIndividualCandidacyProcess>> getValidSecondCycleIndividualCandidaciesByDegree() {
        final Map<Degree, SortedSet<SecondCycleIndividualCandidacyProcess>> result =
                new TreeMap<Degree, SortedSet<SecondCycleIndividualCandidacyProcess>>(Degree.COMPARATOR_BY_NAME_AND_ID);
        for (final IndividualCandidacyProcess child : getChildProcesses()) {
            final SecondCycleIndividualCandidacyProcess process = (SecondCycleIndividualCandidacyProcess) child;
            if (process.isCandidacyValid()) {
                addCandidacy(result, process);
            }
        }
        return result;
    }

    private void addCandidacy(final Map<Degree, SortedSet<SecondCycleIndividualCandidacyProcess>> result,
            final SecondCycleIndividualCandidacyProcess process) {
        SortedSet<SecondCycleIndividualCandidacyProcess> values = result.get(process.getCandidacySelectedDegree());
        if (values == null) {
            result.put(process.getCandidacySelectedDegree(), values =
                    new TreeSet<SecondCycleIndividualCandidacyProcess>(
                            SecondCycleIndividualCandidacyProcess.COMPARATOR_BY_CANDIDACY_PERSON));
        }
        values.add(process);
    }

    public List<SecondCycleIndividualCandidacyProcess> getAcceptedSecondCycleIndividualCandidacies() {
        final List<SecondCycleIndividualCandidacyProcess> result = new ArrayList<SecondCycleIndividualCandidacyProcess>();
        for (final IndividualCandidacyProcess child : getChildProcesses()) {
            if (child.isCandidacyValid() && child.isCandidacyAccepted()) {
                result.add((SecondCycleIndividualCandidacyProcess) child);
            }
        }
        return result;
    }

    private static final Set<DegreeType> ALLOWED_DEGREE_TYPES = Sets.newHashSet(DegreeType.BOLONHA_MASTER_DEGREE,
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
    static public class CreateCandidacyPeriod extends Activity<SecondCycleCandidacyProcess> {

        @Override
        public void checkPreConditions(SecondCycleCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, User userView, Object object) {
            final CandidacyProcessBean bean = (CandidacyProcessBean) object;
            return new SecondCycleCandidacyProcess((ExecutionYear) bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
        }
    }

    static private class EditCandidacyPeriod extends Activity<SecondCycleCandidacyProcess> {

        @Override
        public void checkPreConditions(SecondCycleCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, User userView, Object object) {
            final CandidacyProcessBean bean = (CandidacyProcessBean) object;
            process.edit(bean.getStart(), bean.getEnd());
            return process;
        }
    }

    static private class SendToCoordinator extends Activity<SecondCycleCandidacyProcess> {

        @Override
        public void checkPreConditions(SecondCycleCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isInStandBy() && !process.isSentToScientificCouncil()) {
                throw new PreConditionNotValidException();
            }

            if (!process.hasCandidacyPeriod() || !process.hasStarted() || process.hasOpenCandidacyPeriod()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, User userView, Object object) {
            process.setState(CandidacyProcessState.SENT_TO_COORDINATOR);
            return process;
        }
    }

    static private class PrintCandidacies extends Activity<SecondCycleCandidacyProcess> {

        @Override
        public void checkPreConditions(SecondCycleCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, User userView, Object object) {
            return process; // for now, nothing to be done
        }
    }

    static private class ExportCandidacies extends Activity<SecondCycleCandidacyProcess> {

        @Override
        public void checkPreConditions(SecondCycleCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, User userView, Object object) {
            return process; // for now, nothing to be done
        }
    }

    static private class SendToScientificCouncil extends Activity<SecondCycleCandidacyProcess> {

        @Override
        public void checkPreConditions(SecondCycleCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isInStandBy() && !process.isSentToCoordinator()) {
                throw new PreConditionNotValidException();
            }

            if (!process.hasCandidacyPeriod() || !process.hasStarted() || process.hasOpenCandidacyPeriod()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, User userView, Object object) {
            process.setState(CandidacyProcessState.SENT_TO_SCIENTIFIC_COUNCIL);
            return process;
        }
    }

    static private class ViewChildProcessWithMissingRequiredDocumentFiles extends Activity<SecondCycleCandidacyProcess> {
        @Override
        public void checkPreConditions(SecondCycleCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, User userView, Object object) {
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return true;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return false;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return false;
        }

    }

    static private class SelectAvailableDegrees extends Activity<SecondCycleCandidacyProcess> {

        @Override
        public void checkPreConditions(SecondCycleCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, User userView, Object object) {
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

    public List<Degree> getAvailableDegrees() {
        final Set<Degree> degrees = getDegreeSet();
        return degrees.isEmpty() ? Degree.readAllByDegreeType(DegreeType.BOLONHA_MASTER_DEGREE,
                DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) : new ArrayList<Degree>(degrees);
    }

    public List<SecondCycleCandidacyProcess> getNextSecondCyleCandidacyProcesses() {
        List<SecondCycleCandidacyProcess> result = new ArrayList<SecondCycleCandidacyProcess>();

        List<CandidacyPeriod> readAllByType = CandidacyPeriod.readAllByType(SecondCycleCandidacyPeriod.class);

        for (CandidacyPeriod candidacyPeriod : readAllByType) {
            SecondCycleCandidacyPeriod secondCycleCandidacyPeriod = (SecondCycleCandidacyPeriod) candidacyPeriod;
            if (getCandidacyPeriod().getStart().isBefore(candidacyPeriod.getStart())) {
                result.add(secondCycleCandidacyPeriod.getSecondCycleCandidacyProcess());
            }
        }

        return result;
    }

}
