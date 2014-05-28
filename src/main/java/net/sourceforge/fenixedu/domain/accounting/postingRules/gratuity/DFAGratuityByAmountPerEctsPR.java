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
package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DFAGratuityByAmountPerEctsPR extends DFAGratuityByAmountPerEctsPR_Base {

    public static class DFAGratuityByAmountPerEctsPREditor extends DFAGratuityPREditor {

        private Money dfaAmountPerEctsCredit;

        private DFAGratuityByAmountPerEctsPREditor() {
            super();
        }

        public Money getDfaAmountPerEctsCredit() {
            return dfaAmountPerEctsCredit;
        }

        public void setDfaAmountPerEctsCredit(final Money dfaAmountPerEctsCredit) {
            this.dfaAmountPerEctsCredit = dfaAmountPerEctsCredit;
        }

        @Override
        public Object execute() {
            return ((DFAGratuityByAmountPerEctsPR) getDfaGratuityPR()).edit(getBeginDate(), getDfaTotalAmount(),
                    getDfaAmountPerEctsCredit(), getDfaPartialAcceptedPercentage());
        }

        public static DFAGratuityByAmountPerEctsPREditor buildFrom(final DFAGratuityByAmountPerEctsPR rule) {
            final DFAGratuityByAmountPerEctsPREditor result = new DFAGratuityByAmountPerEctsPREditor();
            init(rule, result);
            result.setDfaAmountPerEctsCredit(rule.getDfaAmountPerEctsCredit());

            return result;
        }

        static private void init(final DFAGratuityPR o1, final DFAGratuityPREditor o2) {
            o2.setDfaGratuityPR(o1);
            o2.setDfaPartialAcceptedPercentage(o1.getDfaPartialAcceptedPercentage());
            o2.setDfaTotalAmount(o1.getDfaTotalAmount());
        }

    }

    protected DFAGratuityByAmountPerEctsPR() {
        super();
    }

    public DFAGratuityByAmountPerEctsPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            Money dfaTotalAmount, BigDecimal partialAcceptedPercentage, Money dfaAmountPerEctsCredit) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
                partialAcceptedPercentage, dfaAmountPerEctsCredit);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money dfaTotalAmount, BigDecimal dfaPartialAcceptedPercentage,
            Money dfaAmountPerEctsCredit) {

        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
                dfaPartialAcceptedPercentage);

        checkParameters(dfaAmountPerEctsCredit);
        super.setDfaAmountPerEctsCredit(dfaAmountPerEctsCredit);
    }

    private void checkParameters(Money dfaAmountPerEctsCredit) {
        if (dfaAmountPerEctsCredit == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR.dfaAmountPerEctsCredit.cannot.be.null");
        }
    }

    @Override
    public void setDfaAmountPerEctsCredit(Money dfaAmountPerEctsCredit) {
        throw new DomainException(
                "error.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR.cannot.modify.dfaAmountPerEctsCredit");
    }

    @Override
    protected Money calculateDFAGratuityTotalAmountToPay(final Event event) {
        final Money result;
        final double enrolmentsEctsForRegistration = ((GratuityEvent) event).getEnrolmentsEctsForRegistration();
        result = getDfaAmountPerEctsCredit().multiply(new BigDecimal(enrolmentsEctsForRegistration));
        return result;
    }

    public DFAGratuityByAmountPerEctsPR edit(Money dfaTotalAmount, Money dfaAmountPerEctsCredit,
            BigDecimal partialAcceptedPercentage) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        return edit(new DateTime(), dfaTotalAmount, dfaAmountPerEctsCredit, partialAcceptedPercentage);
    }

    public DFAGratuityByAmountPerEctsPR edit(DateTime startDate, Money dfaTotalAmount, Money dfaAmountPerEctsCredit,
            BigDecimal partialAcceptedPercentage) {
        check(this, RolePredicates.MANAGER_PREDICATE);

        deactivate(startDate);

        return new DFAGratuityByAmountPerEctsPR(startDate, null, getServiceAgreementTemplate(), dfaTotalAmount,
                partialAcceptedPercentage, dfaAmountPerEctsCredit);

    }

    @Deprecated
    public boolean hasDfaAmountPerEctsCredit() {
        return getDfaAmountPerEctsCredit() != null;
    }

}
