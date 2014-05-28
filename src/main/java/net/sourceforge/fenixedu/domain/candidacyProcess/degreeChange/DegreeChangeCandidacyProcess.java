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
package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

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
import net.sourceforge.fenixedu.domain.period.DegreeChangeCandidacyPeriod;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

import com.google.common.collect.Sets;

public class DegreeChangeCandidacyProcess extends DegreeChangeCandidacyProcess_Base {

    static {
        getRelationCandidacyPeriodCandidacyProcess().addListener(
                new RelationAdapter<CandidacyProcess, CandidacyProcessCandidacyPeriod>() {
                    @Override
                    public void beforeAdd(CandidacyProcess candidacyProcess, CandidacyProcessCandidacyPeriod candidacyPeriod) {
                        super.beforeAdd(candidacyProcess, candidacyPeriod);
                        if (candidacyProcess != null && candidacyPeriod != null
                                && candidacyPeriod instanceof DegreeChangeCandidacyPeriod) {
                            if (candidacyPeriod.hasAnyCandidacyProcesses()) {
                                throw new DomainException(
                                        "error.DegreeChangeCandidacyProcess.candidacy.period.already.has.process");
                            }
                        }
                    }
                });
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new EditCandidacyPeriod());
        activities.add(new SendToCoordinator());
        activities.add(new SendToScientificCouncil());
        activities.add(new PrintCandidaciesFromInstitutionDegrees());
        activities.add(new PrintCandidaciesFromExternalDegrees());
        activities.add(new ExportCandidacies());
        activities.add(new SelectAvailableDegrees());
    }

    private DegreeChangeCandidacyProcess() {
        super();
    }

    private DegreeChangeCandidacyProcess(final ExecutionYear executionYear, final DateTime start, final DateTime end) {
        this();
        checkParameters(executionYear, start, end);
        setState(CandidacyProcessState.STAND_BY);
        new DegreeChangeCandidacyPeriod(this, executionYear, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
        if (executionInterval == null) {
            throw new DomainException("error.DegreeChangeCandidacyProcess.invalid.executionInterval");
        }

        if (start == null || end == null || start.isAfter(end)) {
            throw new DomainException("error.DegreeChangeCandidacyProcess.invalid.interval");
        }
    }

    private void edit(final DateTime start, final DateTime end) {
        checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
        getCandidacyPeriod().edit(start, end);
    }

    @Override
    public boolean canExecuteActivity(User userView) {
        return isAllowedToManageProcess(userView);
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    public Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> getValidDegreeChangeIndividualCandidacyProcessesByDegree() {
        final Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> result =
                new TreeMap<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>>(Degree.COMPARATOR_BY_NAME_AND_ID);
        for (final IndividualCandidacyProcess process : getChildProcessesSet()) {
            final DegreeChangeIndividualCandidacyProcess child = (DegreeChangeIndividualCandidacyProcess) process;
            if (child.isCandidacyValid()) {
                addCandidacy(result, child);
            }
        }
        return result;
    }

    public List<DegreeChangeIndividualCandidacyProcess> getValidDegreeChangeIndividualCandidacyProcesses(final Degree degree) {
        if (degree == null) {
            return Collections.emptyList();
        }

        final List<DegreeChangeIndividualCandidacyProcess> result = new ArrayList<DegreeChangeIndividualCandidacyProcess>();
        for (final IndividualCandidacyProcess process : getChildProcessesSet()) {
            final DegreeChangeIndividualCandidacyProcess child = (DegreeChangeIndividualCandidacyProcess) process;
            if (child.isCandidacyValid() && child.hasCandidacyForSelectedDegree(degree)) {
                result.add(child);
            }
        }
        return result;
    }

    public Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> getValidInstitutionIndividualCandidacyProcessesByDegree() {
        final Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> result =
                new TreeMap<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>>(Degree.COMPARATOR_BY_NAME_AND_ID);

        for (final IndividualCandidacyProcess process : getChildProcessesSet()) {
            final DegreeChangeIndividualCandidacyProcess child = (DegreeChangeIndividualCandidacyProcess) process;
            if (child.isCandidacyValid() && !child.getPrecedentDegreeInformation().isCandidacyExternal()) {
                addCandidacy(result, child);
            }
        }

        return result;
    }

    public Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> getValidExternalIndividualCandidacyProcessesByDegree() {
        final Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> result =
                new TreeMap<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>>(Degree.COMPARATOR_BY_NAME_AND_ID);

        for (final IndividualCandidacyProcess process : getChildProcessesSet()) {
            final DegreeChangeIndividualCandidacyProcess child = (DegreeChangeIndividualCandidacyProcess) process;
            if (child.isCandidacyValid() && child.getPrecedentDegreeInformation().isCandidacyExternal()) {
                addCandidacy(result, child);
            }
        }

        return result;
    }

    private void addCandidacy(final Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> result,
            final DegreeChangeIndividualCandidacyProcess process) {
        SortedSet<DegreeChangeIndividualCandidacyProcess> values = result.get(process.getCandidacySelectedDegree());
        if (values == null) {
            result.put(process.getCandidacySelectedDegree(), values =
                    new TreeSet<DegreeChangeIndividualCandidacyProcess>(
                            DegreeChangeIndividualCandidacyProcess.COMPARATOR_BY_CANDIDACY_PERSON));
        }
        values.add(process);
    }

    public List<DegreeChangeIndividualCandidacyProcess> getAcceptedDegreeChangeIndividualCandidacyProcesses() {
        final List<DegreeChangeIndividualCandidacyProcess> result = new ArrayList<DegreeChangeIndividualCandidacyProcess>();
        for (final IndividualCandidacyProcess child : getChildProcessesSet()) {
            if (child.isCandidacyValid() && child.isCandidacyAccepted()) {
                result.add((DegreeChangeIndividualCandidacyProcess) child);
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
    static public class CreateCandidacyPeriod extends Activity<DegreeChangeCandidacyProcess> {
        @Override
        public void checkPreConditions(DegreeChangeCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess dummy, User userView, Object object) {
            final CandidacyProcessBean bean = (CandidacyProcessBean) object;
            return new DegreeChangeCandidacyProcess((ExecutionYear) bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
        }
    }

    static private class EditCandidacyPeriod extends Activity<DegreeChangeCandidacyProcess> {
        @Override
        public void checkPreConditions(DegreeChangeCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, User userView, Object object) {
            final CandidacyProcessBean bean = (CandidacyProcessBean) object;
            process.edit(bean.getStart(), bean.getEnd());
            return process;
        }
    }

    static private class SendToCoordinator extends Activity<DegreeChangeCandidacyProcess> {
        @Override
        public void checkPreConditions(DegreeChangeCandidacyProcess process, User userView) {
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
        protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, User userView, Object object) {
            process.setState(CandidacyProcessState.SENT_TO_COORDINATOR);
            return process;
        }
    }

    static private class SendToScientificCouncil extends Activity<DegreeChangeCandidacyProcess> {
        @Override
        public void checkPreConditions(DegreeChangeCandidacyProcess process, User userView) {
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
        protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, User userView, Object object) {
            process.setState(CandidacyProcessState.SENT_TO_SCIENTIFIC_COUNCIL);
            return process;
        }
    }

    static private class PrintCandidaciesFromInstitutionDegrees extends Activity<DegreeChangeCandidacyProcess> {
        @Override
        public void checkPreConditions(DegreeChangeCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, User userView, Object object) {
            return process; // for now, nothing to be done
        }
    }

    static private class PrintCandidaciesFromExternalDegrees extends Activity<DegreeChangeCandidacyProcess> {
        @Override
        public void checkPreConditions(DegreeChangeCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, User userView, Object object) {
            return process; // for now, nothing to be done
        }
    }

    static private class ExportCandidacies extends Activity<DegreeChangeCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, User userView, Object object) {
            return process; // for now, nothing to be done
        }
    }

    static private class SelectAvailableDegrees extends Activity<DegreeChangeCandidacyProcess> {

        @Override
        public void checkPreConditions(DegreeChangeCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, User userView, Object object) {
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
        return degrees.isEmpty() ? Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE,
                DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) : new ArrayList<Degree>(degrees);
    }

}