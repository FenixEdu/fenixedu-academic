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
package org.fenixedu.academic.service.services.accounting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.events.AnnualEvent;
import org.fenixedu.academic.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.degreeChange.DegreeChangeCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.degreeTransfer.DegreeTransferCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import org.fenixedu.academic.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import pt.ist.fenixframework.FenixFramework;

@Deprecated
public class SearchPaymentCodesByExecutionYear implements AutoCompleteProvider<PaymentCode> {

    @Override
    public Collection<PaymentCode> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final ExecutionYear executionYear = getExecutionYear(argsMap.get("executionYearOid"));

        final Collection<PaymentCode> result = findAnnualEventsPaymentCodeFor(value, executionYear);

        result.addAll(findIndividualCandidaciesEventPaymentCodesFor(DegreeTransferCandidacyProcess.class, value, executionYear));
        result.addAll(findIndividualCandidaciesEventPaymentCodesFor(DegreeChangeCandidacyProcess.class, value, executionYear));
        result.addAll(findIndividualCandidaciesEventPaymentCodesFor(SecondCycleCandidacyProcess.class, value, executionYear));
        result.addAll(findIndividualCandidaciesEventPaymentCodesFor(DegreeCandidacyForGraduatedPersonProcess.class, value,
                executionYear));
        result.addAll(findIndividualCandidaciesEventPaymentCodesFor(DegreeTransferCandidacyProcess.class, value, executionYear));

        return result;
    }

    private Collection<PaymentCode> findIndividualCandidaciesEventPaymentCodesFor(
            final Class<? extends CandidacyProcess> candidacyProcessClazz, final String paymentCodeValue,
            final ExecutionYear executionYear) {
        CandidacyProcess candidacyProcessByExecutionInterval =
                CandidacyProcess.getCandidacyProcessByExecutionInterval(candidacyProcessClazz, executionYear);

        final Collection<PaymentCode> result = new ArrayList<PaymentCode>();
        if (candidacyProcessByExecutionInterval == null) {
            return result;
        }

        Collection<IndividualCandidacyProcess> childProcesses = candidacyProcessByExecutionInterval.getChildProcessesSet();
        for (IndividualCandidacyProcess individualCandidacyProcess : childProcesses) {
            if (individualCandidacyProcess.getCandidacy().getEvent() == null) {
                continue;
            }
            if (individualCandidacyProcess.getCandidacy().getEvent().getAllPaymentCodes().isEmpty()) {
                continue;
            }
            for (PaymentCode paymentCode : individualCandidacyProcess.getCandidacy().getEvent().getAllPaymentCodes()) {

                if (paymentCode.getCode().startsWith(paymentCodeValue)) {
                    result.add(paymentCode);
                }
            }
        }

        return result;
    }

    private Collection<PaymentCode> findAnnualEventsPaymentCodeFor(final String paymentCodeValue,
            final ExecutionYear executionYear) {
        return executionYear.getAnnualEventsSet().stream()
                .flatMap(event -> event.getAllPaymentCodes().stream())
                .filter(code -> code.getCode().startsWith(paymentCodeValue))
                .collect(Collectors.toList());
    }

    private ExecutionYear getExecutionYear(final String oid) {
        return FenixFramework.getDomainObject(oid);
    }

}
