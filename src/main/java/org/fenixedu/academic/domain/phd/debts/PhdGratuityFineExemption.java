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
package org.fenixedu.academic.domain.phd.debts;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class PhdGratuityFineExemption extends PhdGratuityFineExemption_Base {

    public PhdGratuityFineExemption() {
        super();
    }

    public PhdGratuityFineExemption(Person responsible, Event event, String justification) {
        PhdEventExemptionJustification exemptionJustification =
                new PhdEventExemptionJustification(this, PhdEventExemptionJustificationType.FINE_EXEMPTION, event
                        .getWhenOccured().toLocalDate(), justification);
        super.init(responsible, event, exemptionJustification);

        setRootDomainObject(Bennu.getInstance());
        event.recalculateState(new DateTime());
    }

    @Atomic
    public static PhdGratuityFineExemption createPhdGratuityFineExemption(Person responsible, PhdGratuityEvent event,
            String justification) {
        if (event.hasExemptionsOfType(PhdGratuityFineExemption.class)) {
            throw new DomainException("error.already.has.fine.exemption");
        }
        return new PhdGratuityFineExemption(responsible, event, justification);
    }
}
