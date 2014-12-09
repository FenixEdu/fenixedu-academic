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
package org.fenixedu.academic.domain.accounting.events;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.events.insurance.IInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class InsuranceExemption extends InsuranceExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {
            @Override
            public void beforeAdd(Exemption exemption, Event event) {

                if (exemption instanceof InsuranceExemption && event != null) {
                    AnnualEvent annualEvent = (AnnualEvent) event;

                    if (annualEvent.isInsuranceEvent()) {
                        if (((InsuranceEvent) annualEvent).hasInsuranceExemption()) {
                            throw new DomainException(
                                    "error.org.fenixedu.academic.domain.accounting.events.InsuranceExemption.event.already.has.exemption");
                        }
                    } else if (annualEvent.isAdministrativeOfficeAndInsuranceEvent()) {
                        if (((AdministrativeOfficeFeeAndInsuranceEvent) annualEvent).hasInsuranceExemption()) {
                            throw new DomainException(
                                    "error.org.fenixedu.academic.domain.accounting.events.InsuranceExemption.event.already.has.exemption");
                        }
                    }
                }
            }
        });
    }

    public InsuranceExemption() {
        super();
    }

    public InsuranceExemption(Person responsible, IInsuranceEvent administrativeOfficeFeeAndInsuranceEvent,
            InsuranceExemptionJustificationType justificationType, String reason, YearMonthDay dispatchDate) {
        this();
        super.init(responsible, (Event) administrativeOfficeFeeAndInsuranceEvent,
                InsuranceExemptionJustificationFactory.create(this, justificationType, reason, dispatchDate));

        ((Event) administrativeOfficeFeeAndInsuranceEvent).recalculateState(new DateTime());
    }

    @Override
    public boolean isInsuranceExemption() {
        return true;
    }

    @Override
    public boolean isForInsurance() {
        return true;
    }

    public String getKindDescription() {
        return BundleUtil.getString(Bundle.ENUMERATION, this.getClass().getSimpleName() + ".kindDescription");
    }
}
