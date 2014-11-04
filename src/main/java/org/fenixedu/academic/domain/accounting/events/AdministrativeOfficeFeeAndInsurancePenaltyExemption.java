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
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class AdministrativeOfficeFeeAndInsurancePenaltyExemption extends AdministrativeOfficeFeeAndInsurancePenaltyExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {
            @Override
            public void beforeAdd(Exemption exemption, Event event) {
                if (exemption != null && event != null) {
                    if (exemption instanceof AdministrativeOfficeFeeAndInsurancePenaltyExemption) {
                        final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent =
                                ((AdministrativeOfficeFeeAndInsuranceEvent) event);
                        if (administrativeOfficeFeeAndInsuranceEvent.hasAdministrativeOfficeFeeAndInsurancePenaltyExemption()) {
                            throw new DomainException(
                                    "error.accounting.events.AdministrativeOfficeFeeAndInsuranceExemption.event.already.has.exemption.for.fee.and.insurance");
                        }

                    }
                }
            }
        });
    }

    protected AdministrativeOfficeFeeAndInsurancePenaltyExemption() {
        super();
    }

    public AdministrativeOfficeFeeAndInsurancePenaltyExemption(final PenaltyExemptionJustificationType penaltyExemptionType,
            final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent, final Person responsible,
            final String reason, final YearMonthDay directiveCouncilDispatchDate) {
        this();
        super.init(penaltyExemptionType, administrativeOfficeFeeAndInsuranceEvent, responsible, reason,
                directiveCouncilDispatchDate);
    }

}
