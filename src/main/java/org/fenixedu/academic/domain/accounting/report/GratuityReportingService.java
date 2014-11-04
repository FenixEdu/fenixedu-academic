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
package org.fenixedu.academic.domain.accounting.report;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Collection;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.events.AnnualEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.predicate.RolePredicates;
import org.joda.time.LocalDate;

public class GratuityReportingService {

    public GratuityReport createGratuityReport(final ExecutionYear executionYear, final LocalDate startDate,
            final LocalDate endDate, final Collection<DegreeType> degreeTypes) {
        check(this, RolePredicates.DIRECTIVE_COUNCIL_PREDICATE);

        final GratuityReport report = new GratuityReport();

        for (final AnnualEvent event : executionYear.getAnnualEventsSet()) {

            if (event instanceof GratuityEvent) {
                final GratuityEvent gratuityEvent = (GratuityEvent) event;
                if (!degreeTypes.isEmpty() && !degreeTypes.contains(gratuityEvent.getDegree().getDegreeType())) {
                    continue;
                }

                for (final AccountingTransaction transaction : event.getNonAdjustingTransactions()) {
                    if (startDate != null && endDate != null) {
                        if (transaction.isInsidePeriod(startDate, endDate)) {
                            report.addGratuityAmount(transaction.getWhenRegistered().toLocalDate(),
                                    transaction.getAmountWithAdjustment());
                        }
                    } else if (startDate != null) {
                        if (!transaction.getWhenRegistered().toLocalDate().isBefore(startDate)) {
                            report.addGratuityAmount(transaction.getWhenRegistered().toLocalDate(),
                                    transaction.getAmountWithAdjustment());
                        }
                    } else if (endDate != null) {
                        if (!transaction.getWhenRegistered().toLocalDate().isAfter(endDate)) {
                            report.addGratuityAmount(transaction.getWhenRegistered().toLocalDate(),
                                    transaction.getAmountWithAdjustment());
                        }
                    } else {
                        report.addGratuityAmount(transaction.getWhenRegistered().toLocalDate(),
                                transaction.getAmountWithAdjustment());
                    }
                }

            }
        }

        return report;
    }
}