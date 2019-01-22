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
package org.fenixedu.academic.domain.accounting.postingRules.gratuity;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.gratuity.PastDegreeGratuityEvent;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class PastDegreeGratuityPR extends PastDegreeGratuityPR_Base {

    protected PastDegreeGratuityPR() {

    }

    public PastDegreeGratuityPR(DateTime startDate, DateTime endDate,
            DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate) {
        super.init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event) {
        return ((PastDegreeGratuityEvent) event).getPastDegreeGratuityAmount();
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
