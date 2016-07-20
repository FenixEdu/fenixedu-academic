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
package org.fenixedu.academic.domain.accounting.events.gratuity;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.io.IOUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.util.Money;

public class ExternalScholarshipGratuityExemption extends ExternalScholarshipGratuityExemption_Base {


    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {

            @Override
            public void beforeAdd(Exemption exemption, Event event) {
                if (event instanceof GratuityEvent) {
                    if (exemption instanceof ExternalScholarshipGratuityExemption) {
                        if (event.getExemptionsSet().stream().anyMatch(e->e instanceof ExternalScholarshipGratuityExemption)) {
                            throw new DomainException("error.accounting.events.gratuity.ExternalScholarshipGratuityExemption.event.already.has.scholarship.exemption");
                        }
                    }
                }
            }
        });
    }



    public ExternalScholarshipGratuityExemption(Person responsible, GratuityEvent gratuityEvent,
            Money value,
            ExternalScholarshipGratuityExemptionJustificationType justificationType, String reason, Party creditor,
            String fileName, InputStream file) {
        super();
        init(responsible, gratuityEvent, value, justificationType, reason, creditor, fileName,
                file);
    }

    protected void init(Person responsible, GratuityEvent gratuityEvent,
            Money value, ExternalScholarshipGratuityExemptionJustificationType
            justificationType,
            String reason, Party creditor, String fileName, InputStream content) {
        super.init(responsible, gratuityEvent, justificationType.justification(this, reason));
        setValue(value);
        setParty(creditor);
        try {
            setDocument(new GratuityContributionFile(creditor,gratuityEvent,fileName, IOUtils.toByteArray(content)));
        } catch (IOException e) {
            throw new DomainException("error.accounting.events.gratuity.ExternalScholarshipGratuityExemption.failed.to.read.file",e);
        }
        setExternalScholarshipGratuityContributionEvent(new ExternalScholarshipGratuityContributionEvent(getParty()));
    }

    public BigDecimal calculateDiscountPercentage(Money amount) {
        final BigDecimal amountToDiscount = new BigDecimal(getValue().toString());
        return amountToDiscount.divide(amount.getAmount(), 10, RoundingMode.HALF_EVEN);
    }

    public String getPartyDescription() {
        Party party = getParty();
        return party.getSocialSecurityNumber() + " - " + party.getName();
    }


    public void doDelete() {
        setExternalScholarshipGratuityContributionEvent(null);
        setParty(null);
        super.delete();
    }
}
