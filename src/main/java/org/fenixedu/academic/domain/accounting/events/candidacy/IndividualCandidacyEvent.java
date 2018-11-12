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
package org.fenixedu.academic.domain.accounting.events.candidacy;

import java.util.Collections;
import java.util.Set;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacy;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.SibsTransactionDetailDTO;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;

public abstract class IndividualCandidacyEvent extends IndividualCandidacyEvent_Base {

    protected IndividualCandidacyEvent() {
        super();
    }

    protected void init(final IndividualCandidacy candidacy, final EventType eventType, final Person person) {
        final AdministrativeOffice administrativeOffice = readAdministrativeOffice();
        checkParameters(candidacy, administrativeOffice);
        super.init(administrativeOffice, eventType, person);
        setIndividualCandidacy(candidacy);
    }

    protected void checkParameters(final IndividualCandidacy candidacy, final AdministrativeOffice administrativeOffice) {
        if (candidacy == null) {
            throw new DomainException("error.IndividualCandidacyEvent.invalid.candidacy");
        }
        if (administrativeOffice == null) {
            throw new DomainException("error.IndividualCandidacyEvent.invalid.administrativeOffice");
        }
    }

    abstract protected AdministrativeOffice readAdministrativeOffice();

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {

        LabelFormatter labelFormatter = new LabelFormatter().appendLabel(entryType.name(), Bundle
                .ENUMERATION);

        labelFormatter.appendLabel(" - ");
        for (Degree s : getIndividualCandidacy().getAllDegrees()) {
            labelFormatter.appendLabel(s.getSigla()).appendLabel(" ");
        }
        labelFormatter.appendLabel(" - ").appendLabel(getIndividualCandidacy().getCandidacyDate().toString());

        return labelFormatter;
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getExternalAccount();
    }

    @Override
    public PostingRule getPostingRule() {
        return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
                getWhenOccured());
    }

    @Override
    public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getInternalAccount();
    }

    public Student getCandidacyStudent() {
        return getIndividualCandidacy().getStudent();
    }

    public boolean hasCandidacyStudent() {
        return getIndividualCandidacy().hasStudent();
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, PaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser, Collections.singletonList(new EntryDTO(getEntryType(), this, amountToPay)),
                transactionDetail);
    }

    @Override
    public boolean isIndividualCandidacyEvent() {
        return true;
    }

}
