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
package net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateInstallmentPenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class CreateInstallmentPenaltyExemption {

    @Atomic
    public static void run(final Person responsible, final CreateInstallmentPenaltyExemptionBean penaltyExemptionBean) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        for (final Installment installment : penaltyExemptionBean.getInstallments()) {
            new InstallmentPenaltyExemption(penaltyExemptionBean.getJustificationType(),
                    penaltyExemptionBean.getGratuityEventWithPaymentPlan(), responsible, installment,
                    penaltyExemptionBean.getReason(), penaltyExemptionBean.getDispatchDate());
        }
    }

}