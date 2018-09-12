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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityContributionFile;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;

/**
 * Use {@link org.fenixedu.academic.domain.accounting.events.EventExemption}
 */
@Deprecated
public class PhdGratuityExternalScholarshipExemption extends PhdGratuityExternalScholarshipExemption_Base {

    private PhdGratuityExternalScholarshipExemption() { }

    @Override
    public LabelFormatter getDescription() {
        PhdGratuityEvent event = (PhdGratuityEvent) getEvent();
        final ExecutionYear executionYear = event.getPhdIndividualProgramProcess().getExecutionYear();
        return new LabelFormatter()
                .appendLabel(
                        "Bolsa de entidade externa (" + getParty().getName()
                                + ") aplicada à Propina do Programa de Doutoramento de ")
                .appendLabel(event.getPhdProgram().getName(executionYear).getContent()).appendLabel(" referente a " + event.getYear());
    }

    public void doDelete() {
        setExternalScholarshipPhdGratuityContribuitionEvent(null);
        setParty(null);
        GratuityContributionFile document = getDocument();
        if(document != null) {
            setDocument(null);
            document.delete();
        }
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
