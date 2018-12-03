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
package org.fenixedu.academic.domain.accounting;

import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.events.EventExemptionJustification;
import org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType;
import org.fenixedu.academic.domain.accounting.events.ExemptionJustification;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public abstract class Exemption extends Exemption_Base {

    protected Exemption() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setWhenCreated(new DateTime());
    }

    protected void init(final Person responsible, final Event event, final ExemptionJustification exemptionJustification) {
        checkParameters(event, exemptionJustification);
        super.setResponsible(responsible);
        super.setEvent(event);
        super.setExemptionJustification(exemptionJustification);
    }

    private void checkParameters(final Event event, final ExemptionJustification exemptionJustification) {
        if (event == null) {
            throw new DomainException("error.accounting.Exemption.event.cannot.be.null");
        }
        if (exemptionJustification == null) {
            throw new DomainException("error.accounting.Exemption.exemptionJustification.cannot.be.null");
        }

        //check for operations after this one being created
        
        final List<String> operationsAfter = event.getOperationsAfter(getWhenCreated());
        if (!operationsAfter.isEmpty()) {
            throw new DomainException("error.accounting.Exemption.cannot.create.operations.after",
                    String.join(",", operationsAfter));
        }
    }

    protected ExemptionJustification createJustification(EventExemptionJustificationType justificationType, LocalDate dispatchDate, String reason) {
        return new EventExemptionJustification(this, justificationType, dispatchDate, reason);
    }

    public void delete() {
        delete(true);
    }

    public void delete(final boolean recalculateEventState) {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        setRootDomainObject(null);
        super.setResponsible(null);
        getExemptionJustification().delete();
        final Event event = getEvent();
        super.setEvent(null);
        if (recalculateEventState) {
            event.recalculateState(new DateTime());
        }

        super.deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        blockers.addAll(getEvent().getOperationsAfter(getWhenCreated()));
    }

    public void removeResponsible() {
        super.setResponsible(null);
    }

    public void removeEvent() {
        super.setEvent(null);
    }

    public LabelFormatter getDescription() {
        return getExemptionJustification().getDescription();
    }

    public String getReason() {
        return getExemptionJustification().getReason();
    }

    public boolean isAdministrativeOfficeFeeAndInsuranceExemption() {
        return false;
    }

    public boolean isAdministrativeOfficeFeeExemption() {
        return false;
    }

    public boolean isForAdministrativeOfficeFee() {
        return false;
    }

    public boolean isInsuranceExemption() {
        return false;
    }

    public boolean isForInsurance() {
        return false;
    }

    public boolean isAcademicEventExemption() {
        return false;
    }

    public boolean isGratuityExemption() {
        return false;
    }

    public boolean isPenaltyExemption() {
        return false;
    }

    public boolean isPhdEventExemption() {
        return false;
    }

    public boolean isSecondCycleIndividualCandidacyExemption() {
        return false;
    }

    public abstract Money getExemptionAmount(Money money);

}
