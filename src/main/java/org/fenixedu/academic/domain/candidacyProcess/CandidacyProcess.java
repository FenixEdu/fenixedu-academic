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
package org.fenixedu.academic.domain.candidacyProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

abstract public class CandidacyProcess extends CandidacyProcess_Base {

    protected CandidacyProcess() {
        super();
    }

    public ExecutionInterval getCandidacyExecutionInterval() {
        return getCandidacyPeriod() != null ? getCandidacyPeriod().getExecutionInterval() : null;
    }

    public DateTime getCandidacyStart() {
        return getCandidacyPeriod() != null ? getCandidacyPeriod().getStart() : null;
    }

    public DateTime getCandidacyEnd() {
        return getCandidacyPeriod() != null ? getCandidacyPeriod().getEnd() : null;
    }

    public boolean hasStarted() {
        return !getCandidacyStart().isAfterNow();
    }

    public boolean hasOpenCandidacyPeriod() {
        return getCandidacyPeriod() != null && getCandidacyPeriod().isOpen();
    }

    public boolean hasOpenCandidacyPeriod(final DateTime date) {
        return getCandidacyPeriod() != null && getCandidacyPeriod().isOpen(date);
    }

    public boolean hasState() {
        return getState() != null;
    }

    public boolean isInStandBy() {
        return getState() == CandidacyProcessState.STAND_BY;
    }

    public boolean isSentToJury() {
        return getState() == CandidacyProcessState.SENT_TO_JURY;
    }

    public boolean isSentToCoordinator() {
        return getState() == CandidacyProcessState.SENT_TO_COORDINATOR;
    }

    public boolean isSentToScientificCouncil() {
        return getState() == CandidacyProcessState.SENT_TO_SCIENTIFIC_COUNCIL;
    }

    public boolean isPublished() {
        return getState() == CandidacyProcessState.PUBLISHED;
    }

    @Override
    public String getDisplayName() {
        return BundleUtil.getString(Bundle.CASE_HANDLEING, getClass().getSimpleName());
    }

    public static <T extends CandidacyProcess> T getCandidacyProcessByDate(Class<T> clazz, final DateTime date) {
        return getAllInstancesOf(clazz).stream()
                .filter(process -> process.getCandidacyPeriod() != null)
                .filter(process -> process.getCandidacyPeriod().isOpen(date))
                .findFirst().orElse(null);
    }

    public static <T extends CandidacyProcess> T getCandidacyProcessByExecutionInterval(Class<T> clazz,
            final ExecutionInterval executionInterval) {
        return getAllInstancesOf(clazz).stream()
                .filter(process -> process.getCandidacyPeriod() != null)
                .filter(process -> executionInterval.equals(process.getCandidacyPeriod().getExecutionInterval()))
                .findFirst().orElse(null);
    }

    public IndividualCandidacyProcess getChildProcessByDocumentId(IDDocumentType type, String identification) {
        return getChildProcessesSet().stream()
                .filter(process -> process.getCandidacy() != null)
                .filter(process -> identification.equals(process.getCandidacy().getPersonalDetails().getDocumentIdNumber()))
                .filter(process -> type.equals(process.getCandidacy().getPersonalDetails().getIdDocumentType()))
                .findFirst().orElse(null);
    }

    public IndividualCandidacyProcess getOpenChildProcessByDocumentId(IDDocumentType documentType, String documentId) {
        return this.getChildProcessesSet().stream()
                .filter(child -> documentId.equalsIgnoreCase(child.getCandidacy().getPersonalDetails().getDocumentIdNumber()))
                .filter(child -> !child.isCandidacyCancelled())
                .findFirst().orElse(null);
    }

    public IndividualCandidacyProcess getOpenChildProcessByEidentifier(final String eidentifier) {
        if (StringUtils.isEmpty(eidentifier)) {
            return null;
        }

        return this.getChildProcessesSet().stream().filter(child -> !child.isCandidacyCancelled())
                .filter(child -> !StringUtils.isEmpty(child.getPersonalDetails().getEidentifier()))
                .filter(child -> eidentifier.equalsIgnoreCase(child.getPersonalDetails().getEidentifier()))
                .findFirst().orElse(null);
    }

    public List<IndividualCandidacyProcess> getChildsWithMissingRequiredDocuments() {
        return getChildProcessesSet().stream()
                .filter(process -> !process.isCandidacyCancelled())
                .filter(IndividualCandidacyProcess::isProcessMissingRequiredDocumentFiles)
                .collect(Collectors.toList());
    }

    public List<IndividualCandidacyProcess> getChildsWithMissingShifts() {
        List<IndividualCandidacyProcess> childs = new ArrayList<>();

        CollectionUtils.select(getChildProcessesSet(), arg0 -> {
            boolean hasNotMissingShifts = true;
            IndividualCandidacyProcess process = (IndividualCandidacyProcess) arg0;
            for (Attends attends : process.getCandidacy().getPersonalDetails().getPerson().getCurrentAttends()) {
                if (!attends.hasAllShiftEnrolments()) {
                    hasNotMissingShifts = false;
                }
            }
            return !process.isCandidacyCancelled() && !hasNotMissingShifts;
        }, childs);

        return childs;
    }

    public boolean isMobility() {
        return false;
    }

    public String getPresentationName() {
        String presentationName = "";
        if (getName() != null && !getName().isEmpty()) {
            presentationName += getName() + " - ";
        }
        presentationName += getCandidacyPeriod().getPresentationName();
        return presentationName;
    }

    private static <T extends CandidacyProcess> Set<T> getAllInstancesOf(final Class<? extends T> type) {
        return Bennu.getInstance().getProcessesSet().stream().filter((type)::isInstance).map(p -> (T) p)
                .collect(Collectors.toSet());
    }
}
