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
package net.sourceforge.fenixedu.domain.accounting.report;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.LocalDate;

public class GratuityReportingService {

    public GratuityReport createGratuityReport(final ExecutionYear executionYear, final LocalDate startDate,
            final LocalDate endDate, final Collection<DegreeType> degreeTypes) {
        check(this, RolePredicates.DIRECTIVE_COUNCIL_PREDICATE);

        final GratuityReport report = new GratuityReport();

        for (final AnnualEvent event : executionYear.getAnnualEvents()) {

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