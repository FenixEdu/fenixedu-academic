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
package org.fenixedu.academic.domain.phd.debts;

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

public class ExternalScholarshipPhdGratuityContribuitionEvent extends ExternalScholarshipPhdGratuityContribuitionEvent_Base {
    public ExternalScholarshipPhdGratuityContribuitionEvent(Party party) {
        super();
        init(EventType.EXTERNAL_SCOLARSHIP, party);
        setRootDomainObject(Bennu.getInstance());
    }

    @Override
    protected void disconnect() {
        PhdGratuityExternalScholarshipExemption exemption = getPhdGratuityExternalScholarshipExemption();
        exemption.doDelete();
        super.disconnect();
    }

    public Money calculateAmountToPay() {
        return calculateAmountToPay(new DateTime());
    }

    public Money getTotalValue() {
        return getPhdGratuityExternalScholarshipExemption().getValue();
    }

    @Override
    protected Account getFromAccount() {
        return getParty().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
        return ((PhdGratuityEvent) getPhdGratuityExternalScholarshipExemption().getEvent()).getPhdProgram().getPhdProgramUnit()
                .getAccountBy(AccountType.INTERNAL);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        return new LabelFormatter()
                .appendLabel(entryType.name(), Bundle.ENUMERATION)
                .appendLabel(" (")
                .appendLabel(
                        ((PhdGratuityEvent) getPhdGratuityExternalScholarshipExemption().getEvent()).getPhdProgram().getName()
                                .getContent()).appendLabel(")");
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

    @Override
    public boolean isFctScholarshipPhdGratuityContribuitionEvent() {
        return true;
    }

}
