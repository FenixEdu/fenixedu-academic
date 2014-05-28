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
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DFAGratuityByNumberOfEnrolmentsPR extends DFAGratuityByNumberOfEnrolmentsPR_Base {

    private static final int SCALE_FOR_INTERMEDIATE_CALCULATIONS = 8;

    public static class DFAGratuityByNumberOfEnrolmentsPREditor extends DFAGratuityPREditor {

        private DFAGratuityByNumberOfEnrolmentsPREditor() {
            super();
        }

        @Override
        public Object execute() {
            return ((DFAGratuityByNumberOfEnrolmentsPR) getDfaGratuityPR()).edit(getBeginDate(), getDfaTotalAmount(),
                    getDfaPartialAcceptedPercentage());
        }

        public static DFAGratuityByNumberOfEnrolmentsPREditor buildFrom(final DFAGratuityByNumberOfEnrolmentsPR rule) {
            final DFAGratuityByNumberOfEnrolmentsPREditor result = new DFAGratuityByNumberOfEnrolmentsPREditor();
            init(rule, result);

            return result;
        }

        static private void init(final DFAGratuityPR o1, final DFAGratuityPREditor o2) {
            o2.setDfaGratuityPR(o1);
            o2.setDfaPartialAcceptedPercentage(o1.getDfaPartialAcceptedPercentage());
            o2.setDfaTotalAmount(o1.getDfaTotalAmount());
        }

    }

    protected DFAGratuityByNumberOfEnrolmentsPR() {
        super();
    }

    public DFAGratuityByNumberOfEnrolmentsPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money dfaTotalAmount, BigDecimal partialAcceptedPercentage) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
                partialAcceptedPercentage);
    }

    @Override
    protected Money calculateDFAGratuityTotalAmountToPay(final Event event) {
        final GratuityEvent gratuityEvent = (GratuityEvent) event;
        final BigDecimal numberOfEnrolments = BigDecimal.valueOf(gratuityEvent.getEnrolmentsEctsForRegistration());
        final BigDecimal ectsCredits =
                BigDecimal.valueOf(gratuityEvent.getStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE)
                        .getDefaultEcts(gratuityEvent.getExecutionYear()));

        final Money result =
                getDfaTotalAmount().multiply(
                        numberOfEnrolments.divide(ectsCredits, SCALE_FOR_INTERMEDIATE_CALCULATIONS, RoundingMode.HALF_EVEN));
        return result.lessOrEqualThan(getDfaTotalAmount()) ? result : getDfaTotalAmount();
    }

    public DFAGratuityByNumberOfEnrolmentsPR edit(Money dfaTotalAmount, BigDecimal partialAcceptedPercentage) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        return edit(new DateTime(), dfaTotalAmount, partialAcceptedPercentage);
    }

    public DFAGratuityByNumberOfEnrolmentsPR edit(DateTime startDate, Money dfaTotalAmount, BigDecimal partialAcceptedPercentage) {
        check(this, RolePredicates.MANAGER_PREDICATE);

        deactivate(startDate);

        return new DFAGratuityByNumberOfEnrolmentsPR(startDate, null, getServiceAgreementTemplate(), dfaTotalAmount,
                partialAcceptedPercentage);

    }

}
