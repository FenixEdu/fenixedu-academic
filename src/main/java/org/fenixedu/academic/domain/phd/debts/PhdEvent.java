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
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.PhdProgramUnit;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

abstract public class PhdEvent extends PhdEvent_Base {

    protected PhdEvent() {
        super();
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
        return getUnit().getAccountBy(AccountType.INTERNAL);
    }

    protected PhdProgramUnit getUnit() {
        return getPhdProgram().getPhdProgramUnit();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
        return new LabelFormatter().appendLabel(entryType.name(), Bundle.ENUMERATION).appendLabel(" (")
                .appendLabel(getPhdProgram().getName().getContent()).appendLabel(")");
    }

    abstract protected PhdProgram getPhdProgram();

    @Override
    public PostingRule getPostingRule() {
        return getPhdProgram().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    public boolean hasPhdEventExemption() {
        return getPhdEventExemption() != null;
    }

    public PhdEventExemption getPhdEventExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption instanceof PhdEventExemption) {
                return (PhdEventExemption) exemption;
            }
        }
        return null;
    }

    @Override
    public boolean isPhdEvent() {
        return true;
    }

    public abstract PhdIndividualProgramProcess getPhdIndividualProgramProcess();

    public boolean isPhdThesisRequestFee() {
        return false;
    }
}
