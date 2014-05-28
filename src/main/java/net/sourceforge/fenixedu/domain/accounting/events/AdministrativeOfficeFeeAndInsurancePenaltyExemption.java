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
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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
