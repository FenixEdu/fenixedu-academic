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
package org.fenixedu.academic.domain.accounting.postingRules.candidacy;

import java.util.List;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.candidacy.DegreeTransferIndividualCandidacyEvent;
import org.fenixedu.academic.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacy;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class DegreeTransferIndividualCandidacyPR extends DegreeTransferIndividualCandidacyPR_Base {

    private DegreeTransferIndividualCandidacyPR() {
        super();
    }

    public DegreeTransferIndividualCandidacyPR(final DateTime start, final DateTime end,
            final ServiceAgreementTemplate agreementTemplate, final Money amountForInstitutionStudent,
            final Money amountForExternalStudent) {

        this();
        super.init(EntryType.DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_FEE, EventType.DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY, start,
                end, agreementTemplate);
        checkParameters(amountForInstitutionStudent, amountForExternalStudent);
        super.setAmountForInstitutionStudent(amountForInstitutionStudent);
        super.setAmountForExternalStudent(amountForExternalStudent);
    }

    private void checkParameters(final Money amountForInstitutionStudent, final Money amountForExternalStudent) {
        if (amountForInstitutionStudent == null) {
            throw new DomainException("error.DegreeTransferIndividualCandidacyPR.invalid.amountForInstitutionStudent");
        }
        if (amountForExternalStudent == null) {
            throw new DomainException("error.DegreeTransferIndividualCandidacyPR.invalid.amountForExternalStudent");
        }
    }

    @Override
    public void setAmountForInstitutionStudent(final Money amountForInstitutionStudent) {
        throw new DomainException("error.DegreeTransferIndividualCandidacyPR.cannot.modify.amountForInstitutionStudent");
    }

    @Override
    public void setAmountForExternalStudent(final Money amountForExternalStudent) {
        throw new DomainException("error.DegreeTransferIndividualCandidacyPR.cannot.modify.amountForExternalStudent");
    }

    @Override
    protected Money doCalculationForAmountToPay(Event eventArg, DateTime when) {
        final DegreeTransferIndividualCandidacyEvent event = (DegreeTransferIndividualCandidacyEvent) eventArg;
        DegreeTransferIndividualCandidacy individualCandidacy = event.getIndividualCandidacy();
        final PrecedentDegreeInformation information = individualCandidacy.getRefactoredPrecedentDegreeInformation();

        if (individualCandidacy.getUtlStudent() != null) {
            return individualCandidacy.getUtlStudent() ? getAmountForInstitutionStudent() : getAmountForExternalStudent();
        } else {
            if (information.isCandidacyInternal() || hasAnyValidRegistration(event)
                    || belongsToInstitutionGroup(information.getInstitution())) {
                return getAmountForInstitutionStudent();
            } else {
                return getAmountForExternalStudent();
            }
        }
    }

    private boolean hasAnyValidRegistration(final DegreeTransferIndividualCandidacyEvent event) {
        if (!event.hasCandidacyStudent()) {
            return false;
        }

        final List<Registration> registrations = event.getCandidacyStudent().getRegistrationsFor(event.getCandidacyDegree());

        return event.getCandidacyStudent().getRegistrationsSet().stream()
                .anyMatch(registration -> !registrations.contains(registration) && !registration.isCanceled());
    }

    private boolean belongsToInstitutionGroup(final Unit unit) {
        return getRootDomainObject().getInstitutionUnit().getParentUnits().stream().anyMatch(parent -> parent.hasSubUnit(unit));
    }

    public DegreeTransferIndividualCandidacyPR edit(final Money amountForInstitutionStudent, final Money amountForExternalStudent) {
        deactivate();
        return new DegreeTransferIndividualCandidacyPR(new DateTime(), null, getServiceAgreementTemplate(),
                amountForInstitutionStudent, amountForExternalStudent);
    }

}
