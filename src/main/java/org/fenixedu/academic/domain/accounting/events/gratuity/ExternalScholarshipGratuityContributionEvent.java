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
package org.fenixedu.academic.domain.accounting.events.gratuity;

import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountType;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class ExternalScholarshipGratuityContributionEvent extends ExternalScholarshipGratuityContributionEvent_Base {
    public ExternalScholarshipGratuityContributionEvent(Party party) {
        super();
        init(EventType.EXTERNAL_CONTRIBUTION, party);
        setRootDomainObject(Bennu.getInstance());
    }

    @Override
    protected void disconnect() {
        ExternalScholarshipGratuityExemption exemption = getExternalScholarshipGratuityExemption();
        exemption.doDelete();
        super.disconnect();
    }

    public Money calculateAmountToPay() {
        return calculateAmountToPay(new DateTime());
    }

    public Money getTotalValue() {
        return getExternalScholarshipGratuityExemption().getValue();
    }

    @Override
    protected Account getFromAccount() {
        return getParty().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
        return getExternalScholarshipGratuityExemption().getEvent().getToAccount();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        return new LabelFormatter()
                .appendLabel(entryType.name(), Bundle.ENUMERATION)
                .appendLabel(" (")
                .appendLabel(
                        ((GratuityEvent) getExternalScholarshipGratuityExemption().getEvent()).getStudentCurricularPlan().getName()).appendLabel(")");
    }

    @Override
    public PostingRule getPostingRule() {
        return AdministrativeOffice.readMasterDegreeAdministrativeOffice().getServiceAgreementTemplate()
                .findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }

    @Override
    public Unit getOwnerUnit() {
        return AdministrativeOffice.readMasterDegreeAdministrativeOffice().getUnit();
    }

}