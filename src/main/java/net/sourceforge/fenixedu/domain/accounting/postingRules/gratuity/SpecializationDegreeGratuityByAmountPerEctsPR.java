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

public class SpecializationDegreeGratuityByAmountPerEctsPR extends SpecializationDegreeGratuityByAmountPerEctsPR_Base {

    public static class SpecializationDegreeGratuityByAmountPerEctsPREditor extends SpecializationDegreeGratuityPREditor {

        private Money specializationDegreeAmountPerEctsCredit;

        private SpecializationDegreeGratuityByAmountPerEctsPREditor() {
            super();
        }

        public Money getSpecializationDegreeAmountPerEctsCredit() {
            return specializationDegreeAmountPerEctsCredit;
        }

        public void setSpecializationDegreeAmountPerEctsCredit(final Money specializationDegreeAmountPerEctsCredit) {
            this.specializationDegreeAmountPerEctsCredit = specializationDegreeAmountPerEctsCredit;
        }

        @Override
        public Object execute() {
            return ((SpecializationDegreeGratuityByAmountPerEctsPR) getSpecializationDegreeGratuityPR()).edit(getBeginDate(),
                    getSpecializationDegreeTotalAmount(), getSpecializationDegreeAmountPerEctsCredit(),
                    getSpecializationDegreePartialAcceptedPercentage());
        }

        public static SpecializationDegreeGratuityByAmountPerEctsPREditor buildFrom(
                final SpecializationDegreeGratuityByAmountPerEctsPR rule) {
            final SpecializationDegreeGratuityByAmountPerEctsPREditor result =
                    new SpecializationDegreeGratuityByAmountPerEctsPREditor();
            init(rule, result);
            result.setSpecializationDegreeAmountPerEctsCredit(rule.getSpecializationDegreeAmountPerEctsCredit());

            return result;
        }

        static private void init(final SpecializationDegreeGratuityPR o1, final SpecializationDegreeGratuityPREditor o2) {
            o2.setSpecializationDegreeGratuityPR(o1);
            o2.setSpecializationDegreePartialAcceptedPercentage(o1.getSpecializationDegreePartialAcceptedPercentage());
            o2.setSpecializationDegreeTotalAmount(o1.getSpecializationDegreeTotalAmount());
        }
    }

    protected SpecializationDegreeGratuityByAmountPerEctsPR() {
        super();
    }

    public SpecializationDegreeGratuityByAmountPerEctsPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money specializationDegreeTotalAmount,
            BigDecimal partialAcceptedPercentage, Money specializationDegreeAmountPerEctsCredit) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate,
                specializationDegreeTotalAmount, partialAcceptedPercentage, specializationDegreeAmountPerEctsCredit);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money specializationDegreeTotalAmount,
            BigDecimal specializationDegreePartialAcceptedPercentage, Money specializationDegreeAmountPerEctsCredit) {

        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, specializationDegreeTotalAmount,
                specializationDegreePartialAcceptedPercentage);

        checkParameters(specializationDegreeAmountPerEctsCredit);
        super.setSpecializationDegreeAmountPerEctsCredit(specializationDegreeAmountPerEctsCredit);
    }

    private void checkParameters(Money specializationDegreeAmountPerEctsCredit) {
        if (specializationDegreeAmountPerEctsCredit == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityByAmountPerEctsPR.specializationDegreeAmountPerEctsCredit.cannot.be.null");
        }
    }

    @Override
    public void setSpecializationDegreeAmountPerEctsCredit(Money specializationDegreeAmountPerEctsCredit) {
        throw new DomainException(
                "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityByAmountPerEctsPR.cannot.modify.specializationDegreeAmountPerEctsCredit");
    }

    @Override
    protected Money calculateSpecializationDegreeGratuityTotalAmountToPay(final Event event) {
        final Money result;
        final double enrolmentsEctsForRegistration = ((GratuityEvent) event).getEnrolmentsEctsForRegistration();
        result = getSpecializationDegreeAmountPerEctsCredit().multiply(new BigDecimal(enrolmentsEctsForRegistration));

        return result;
    }

    public SpecializationDegreeGratuityByAmountPerEctsPR edit(Money specializationDegreeTotalAmount,
            Money specializationDegreeAmountPerEctsCredit, BigDecimal partialAcceptedPercentage) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        return edit(new DateTime(), specializationDegreeTotalAmount, specializationDegreeAmountPerEctsCredit,
                partialAcceptedPercentage);
    }

    public SpecializationDegreeGratuityByAmountPerEctsPR edit(DateTime startDate, Money specializationDegreeTotalAmount,
            Money specializationDegreeAmountPerEctsCredit, BigDecimal partialAcceptedPercentage) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        deactivate(startDate);

        return new SpecializationDegreeGratuityByAmountPerEctsPR(startDate, null, getServiceAgreementTemplate(),
                specializationDegreeTotalAmount, partialAcceptedPercentage, specializationDegreeAmountPerEctsCredit);
    }

    @Deprecated
    public boolean hasSpecializationDegreeAmountPerEctsCredit() {
        return getSpecializationDegreeAmountPerEctsCredit() != null;
    }

}
