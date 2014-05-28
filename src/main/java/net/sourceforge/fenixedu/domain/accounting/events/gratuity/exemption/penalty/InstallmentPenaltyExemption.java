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
package net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class InstallmentPenaltyExemption extends InstallmentPenaltyExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {
            @Override
            public void beforeAdd(Exemption exemption, Event event) {
                if (exemption != null && event != null) {
                    if (exemption instanceof InstallmentPenaltyExemption) {
                        if (!(event instanceof GratuityEventWithPaymentPlan)) {
                            throw new DomainException(
                                    "error.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption.cannot.add.installment.penalty.exemption.to.events.without.payment.plan");
                        }
                    }
                }
            }
        });
    }

    protected InstallmentPenaltyExemption() {
        super();
    }

    public InstallmentPenaltyExemption(final PenaltyExemptionJustificationType penaltyExemptionType,
            final GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan, final Person responsible,
            final Installment installment, final String comments, final YearMonthDay directiveCouncilDispatchDate) {
        this();
        init(penaltyExemptionType, gratuityEventWithPaymentPlan, responsible, installment, comments, directiveCouncilDispatchDate);

    }

    protected void init(PenaltyExemptionJustificationType penaltyExemptionType,
            GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan, Person responsible, Installment installment,
            String comments, YearMonthDay directiveCouncilDispatchDate) {
        super.init(penaltyExemptionType, gratuityEventWithPaymentPlan, responsible, comments, directiveCouncilDispatchDate);
        checkParameters(installment);
        checkRulesToCreate(gratuityEventWithPaymentPlan, installment);
        super.setInstallment(installment);
    }

    private void checkRulesToCreate(final GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan, final Installment installment) {
        if (gratuityEventWithPaymentPlan.hasPenaltyExemptionFor(installment)) {
            throw new DomainException(
                    "error.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption.event.already.has.penalty.exemption.for.installment");

        }

    }

    private void checkParameters(Installment installment) {
        if (installment == null) {
            throw new DomainException(
                    "error.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption.installment.cannot.be.null");
        }
    }

    @Override
    public void setInstallment(Installment installment) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        super.setInstallment(installment);
    }

    @Override
    public void delete() {
        super.setInstallment(null);
        super.delete();
    }

    @Deprecated
    public boolean hasInstallment() {
        return getInstallment() != null;
    }

}
