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
import net.sourceforge.fenixedu.domain.accounting.AdministrativeOfficeFeeAndInsuranceExemptionJustificationFactory;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.administrativeOfficeFee.IAdministrativeOfficeFeeEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class AdministrativeOfficeFeeExemption extends AdministrativeOfficeFeeExemption_Base implements
        IAdministrativeOfficeFeeEvent {
    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {
            @Override
            public void beforeAdd(Exemption exemption, Event event) {

                if (exemption instanceof AdministrativeOfficeFeeAndInsuranceExemption && event != null) {
                    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent =
                            (AdministrativeOfficeFeeAndInsuranceEvent) event;
                    if (administrativeOfficeFeeAndInsuranceEvent.hasAdministrativeOfficeFeeAndInsuranceExemption()) {
                        throw new DomainException(
                                "error.net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemption.event.already.has.exemption");

                    }
                }
            }
        });
    }

    protected AdministrativeOfficeFeeExemption() {
        super();
    }

    public AdministrativeOfficeFeeExemption(Person responsible,
            AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent,
            AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, String reason,
            YearMonthDay dispatchDate) {
        this();

        super.init(responsible, administrativeOfficeFeeAndInsuranceEvent,
                AdministrativeOfficeFeeAndInsuranceExemptionJustificationFactory.create(this, justificationType, reason,
                        dispatchDate));

        administrativeOfficeFeeAndInsuranceEvent.recalculateState(new DateTime());
    }

    @Override
    public boolean isAdministrativeOfficeFeeExemption() {
        return true;
    }

    @Override
    public boolean isForAdministrativeOfficeFee() {
        return true;
    }

    public String getKindDescription() {
        return BundleUtil.getString(Bundle.ENUMERATION, this.getClass().getSimpleName() + ".kindDescription");
    }
}
