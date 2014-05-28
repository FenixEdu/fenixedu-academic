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
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DFAGratuityByEnrolmentsPR extends DFAGratuityByEnrolmentsPR_Base {
    private static final int SCALE_FOR_INTERMEDIATE_CALCULATIONS = 8;

    public static class DFAGratuityByEnrolmentsPREditor extends DFAGratuityPREditor {

        private Money dfaAmountPerEnrolment;

        private DFAGratuityByEnrolmentsPREditor() {
            super();
        }

        @Override
        public Object execute() {
            return ((DFAGratuityByEnrolmentsPR) getDfaGratuityPR()).edit(getBeginDate(), getDfaTotalAmount(),
                    getDfaAmountPerEnrolment(), getDfaPartialAcceptedPercentage());
        }

        public static DFAGratuityByEnrolmentsPREditor buildFrom(final DFAGratuityByEnrolmentsPR rule) {
            final DFAGratuityByEnrolmentsPREditor result = new DFAGratuityByEnrolmentsPREditor();
            init(rule, result);

            return result;
        }

        static private void init(final DFAGratuityPR o1, final DFAGratuityPREditor o2) {
            o2.setDfaGratuityPR(o1);
            o2.setDfaPartialAcceptedPercentage(o1.getDfaPartialAcceptedPercentage());
            o2.setDfaTotalAmount(o1.getDfaTotalAmount());
        }

        public Money getDfaAmountPerEnrolment() {
            return dfaAmountPerEnrolment;
        }

        public void setDfaAmountPerEnrolment(Money dfaAmountPerEnrolment) {
            this.dfaAmountPerEnrolment = dfaAmountPerEnrolment;
        }

    }

    protected DFAGratuityByEnrolmentsPR() {
        super();
    }

    public DFAGratuityByEnrolmentsPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            Money dfaTotalAmount, BigDecimal partialAcceptedPercentage, Money dfaAmountPerEnrolment) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
                partialAcceptedPercentage, dfaAmountPerEnrolment);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money dfaTotalAmount, BigDecimal dfaPartialAcceptedPercentage,
            Money dfaAmountPerEnrolment) {

        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
                dfaPartialAcceptedPercentage);

        checkParameters(dfaAmountPerEnrolment);
        super.setDfaAmountPerEnrolment(dfaAmountPerEnrolment);
    }

    private void checkParameters(Money dfaAmountPerEnrolment) {
        if (dfaAmountPerEnrolment == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR.dfaAmountPerEnrolment.cannot.be.null");
        }
    }

    @Override
    protected Money calculateDFAGratuityTotalAmountToPay(Event event) {
        DfaGratuityEvent dfaGratuity = (DfaGratuityEvent) event;

        return getDfaAmountPerEnrolment().multiply(
                dfaGratuity.getRegistration().getEnrolments(dfaGratuity.getExecutionYear()).size());
    }

    @Override
    public void setDfaAmountPerEnrolment(Money dfaAmountPerEnrolment) {
        throw new DomainException(
                "error.accounting.postingRules.gratuity.DFAGratuityByEnrolmentsPR.cannot.modify.dfaAmountPerEnrolment");
    }

    public DFAGratuityByEnrolmentsPR edit(Money dfaTotalAmount, Money dfaAmountPerEnrolment, BigDecimal partialAcceptedPercentage) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        return edit(new DateTime(), dfaTotalAmount, dfaAmountPerEnrolment, partialAcceptedPercentage);
    }

    public DFAGratuityByEnrolmentsPR edit(DateTime startDate, Money dfaTotalAmount, Money dfaAmountPerEnrolment,
            BigDecimal partialAcceptedPercentage) {
        check(this, RolePredicates.MANAGER_PREDICATE);

        deactivate(startDate);

        return new DFAGratuityByEnrolmentsPR(startDate, null, getServiceAgreementTemplate(), dfaTotalAmount,
                partialAcceptedPercentage, dfaAmountPerEnrolment);
    }

    @Deprecated
    public boolean hasDfaAmountPerEnrolment() {
        return getDfaAmountPerEnrolment() != null;
    }

}
