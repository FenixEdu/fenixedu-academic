/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.candidacyProcess.standalone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcessBean;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcessState;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.caseHandling.StartActivity;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.period.StandaloneCandidacyPeriod;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class StandaloneCandidacyProcess extends StandaloneCandidacyProcess_Base {

    static private final List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new EditCandidacyPeriod());
        activities.add(new SendToCoordinator());
        activities.add(new PrintCandidacies());
    }

    private StandaloneCandidacyProcess() {
        super();
    }

    public StandaloneCandidacyProcess(final ExecutionSemester executionSemester, final DateTime start, final DateTime end) {
        this();
        checkParameters(executionSemester, start, end);
        setState(CandidacyProcessState.STAND_BY);
        new StandaloneCandidacyPeriod(this, executionSemester, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
        if (executionInterval == null) {
            throw new DomainException("error.StandaloneCandidacyProcess.invalid.executionInterval");
        }

        if (start == null || end == null || start.isAfter(end)) {
            throw new DomainException("error.StandaloneCandidacyProcess.invalid.interval");
        }
    }

    @Override
    public boolean canExecuteActivity(final User userView) {
        return isAllowedToManageProcess(userView);
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    private void edit(final DateTime start, final DateTime end) {
        checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
        getCandidacyPeriod().edit(start, end);
    }

    public List<StandaloneIndividualCandidacyProcess> getSortedStandaloneIndividualCandidaciesThatCanBeSendToJury() {
        final List<StandaloneIndividualCandidacyProcess> result = new ArrayList<StandaloneIndividualCandidacyProcess>();
        for (final IndividualCandidacyProcess child : getChildProcessesSet()) {
            if (child.isCandidacyValid()) {
                result.add((StandaloneIndividualCandidacyProcess) child);
            }
        }
        Collections.sort(result, StandaloneIndividualCandidacyProcess.COMPARATOR_BY_CANDIDACY_PERSON);
        return result;
    }

    public List<StandaloneIndividualCandidacyProcess> getAcceptedStandaloneIndividualCandidacies() {
        final List<StandaloneIndividualCandidacyProcess> result = new ArrayList<StandaloneIndividualCandidacyProcess>();
        for (final IndividualCandidacyProcess child : getChildProcessesSet()) {
            if (child.isCandidacyValid() && child.isCandidacyAccepted()) {
                result.add((StandaloneIndividualCandidacyProcess) child);
            }
        }
        return result;
    }

    // static information

    private static final Predicate<DegreeType> ALLOWED_DEGREE_TYPES = DegreeType.oneOf(DegreeType::isBolonhaDegree,
            DegreeType::isBolonhaMasterDegree, DegreeType::isIntegratedMasterDegree, DegreeType::isAdvancedFormationDiploma,
            DegreeType::isIntegratedMasterDegree, DegreeType::isSpecializationDegree);

    static private boolean isAllowedToManageProcess(User userView) {
        for (AcademicProgram program : AcademicAccessRule.getProgramsAccessibleToFunction(
                AcademicOperationType.MANAGE_CANDIDACY_PROCESSES, userView.getPerson().getUser()).collect(Collectors.toSet())) {
            if (ALLOWED_DEGREE_TYPES.test(program.getDegreeType())) {
                return true;
            }
        }
        return false;
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<StandaloneCandidacyProcess> {

        @Override
        public void checkPreConditions(StandaloneCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected StandaloneCandidacyProcess executeActivity(StandaloneCandidacyProcess process, User userView, Object object) {
            final CandidacyProcessBean bean = (CandidacyProcessBean) object;
            return new StandaloneCandidacyProcess((ExecutionSemester) bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
        }
    }

    static private class EditCandidacyPeriod extends Activity<StandaloneCandidacyProcess> {

        @Override
        public void checkPreConditions(StandaloneCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected StandaloneCandidacyProcess executeActivity(StandaloneCandidacyProcess process, User userView, Object object) {
            final CandidacyProcessBean bean = (CandidacyProcessBean) object;
            process.edit(bean.getStart(), bean.getEnd());
            return process;
        }
    }

    static private class SendToCoordinator extends Activity<StandaloneCandidacyProcess> {

        @Override
        public void checkPreConditions(StandaloneCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isInStandBy()) {
                throw new PreConditionNotValidException();
            }

            if (process.getCandidacyPeriod() == null || !process.hasStarted() || process.hasOpenCandidacyPeriod()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected StandaloneCandidacyProcess executeActivity(StandaloneCandidacyProcess process, User userView, Object object) {
            process.setState(CandidacyProcessState.SENT_TO_COORDINATOR);
            return process;
        }
    }

    static private class PrintCandidacies extends Activity<StandaloneCandidacyProcess> {

        @Override
        public void checkPreConditions(StandaloneCandidacyProcess process, User userView) {
            if (!isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected StandaloneCandidacyProcess executeActivity(StandaloneCandidacyProcess process, User userView, Object object) {
            return process; // for now, nothing to be done
        }
    }

}
