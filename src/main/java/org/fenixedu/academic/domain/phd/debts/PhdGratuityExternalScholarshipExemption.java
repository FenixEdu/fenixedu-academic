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
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class PhdGratuityExternalScholarshipExemption extends PhdGratuityExternalScholarshipExemption_Base {

    public PhdGratuityExternalScholarshipExemption(Person responsible, Event event, Party party, Money value) {
        PhdEventExemptionJustification exemptionJustification =
                new PhdEventExemptionJustification(this,
                        PhdEventExemptionJustificationType.PHD_GRATUITY_FCT_SCHOLARSHIP_EXEMPTION, event.getWhenOccured()
                                .toLocalDate(), "Criado pela existencia de bolsa de entidade externa.");
        super.init(responsible, event, exemptionJustification);
        setParty(party);
        createExternalDebt();
        setValue(value);
        setRootDomainObject(Bennu.getInstance());
        event.recalculateState(new DateTime());
    }

    private void createExternalDebt() {
        ExternalScholarshipPhdGratuityContribuitionEvent event = new ExternalScholarshipPhdGratuityContribuitionEvent(getParty());
        this.setExternalScholarshipPhdGratuityContribuitionEvent(event);
    }

    @Atomic
    public static PhdGratuityExternalScholarshipExemption createPhdGratuityExternalScholarshipExemption(Person responsible,
            Money value, Party party, PhdGratuityEvent event) {
        if (event.hasExemptionsOfType(PhdGratuityExternalScholarshipExemption.class)) {
            throw new DomainException("error.already.has.scolarship");
        }
        PhdGratuityExternalScholarshipExemption phdGratuityExternalScholarshipExemption =
                new PhdGratuityExternalScholarshipExemption(responsible, event, party, value);
        return phdGratuityExternalScholarshipExemption;
    }

    @Override
    public LabelFormatter getDescription() {
        PhdGratuityEvent event = (PhdGratuityEvent) getEvent();
        return new LabelFormatter()
                .appendLabel(
                        "Bolsa de entidade externa (" + getParty().getName()
                                + ") aplicada à Propina do Programa de Doutoramento de ")
                .appendLabel(event.getPhdProgram().getName().getContent()).appendLabel(" referente a " + event.getYear());
    }

    public void doDelete() {
        setExternalScholarshipPhdGratuityContribuitionEvent(null);
        setParty(null);
        super.delete();
    }

    @Override
    public void delete() {
        ExternalScholarshipPhdGratuityContribuitionEvent event = getExternalScholarshipPhdGratuityContribuitionEvent();
        event.delete();
    }

    public Money getAmoutStillMissing() {
        return getExternalScholarshipPhdGratuityContribuitionEvent().calculateAmountToPay();
    }

}
