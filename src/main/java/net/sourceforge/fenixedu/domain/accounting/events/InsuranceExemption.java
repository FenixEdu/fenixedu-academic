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
package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.IInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;

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
                                    "error.net.sourceforge.fenixedu.domain.accounting.events.InsuranceExemption.event.already.has.exemption");
                        }
                    } else if (annualEvent.isAdministrativeOfficeAndInsuranceEvent()) {
                        if (((AdministrativeOfficeFeeAndInsuranceEvent) annualEvent).hasInsuranceExemption()) {
                            throw new DomainException(
                                    "error.net.sourceforge.fenixedu.domain.accounting.events.InsuranceExemption.event.already.has.exemption");
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
