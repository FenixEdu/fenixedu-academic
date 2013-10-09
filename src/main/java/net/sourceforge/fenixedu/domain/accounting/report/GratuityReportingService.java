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