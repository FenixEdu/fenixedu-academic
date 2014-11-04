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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.util.Money;

import org.joda.time.LocalDate;

public class GratuityReport {

    public static class GratuityReportEntry {

        private LocalDate date;

        private Money gratuityAmount;

        public Money getGratuityAmount() {
            return gratuityAmount;
        }

        public void addGratuityAmount(Money amount) {
            this.gratuityAmount = gratuityAmount.add(amount);
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public GratuityReportEntry(final LocalDate date) {
            this.date = date;
            this.gratuityAmount = Money.ZERO;
        }

    }

    private Map<LocalDate, GratuityReportEntry> entries;

    public GratuityReport() {
        this.entries = new HashMap<LocalDate, GratuityReportEntry>();
    }

    public void addGratuityAmount(final LocalDate date, final Money amount) {
        if (!this.entries.containsKey(date)) {
            this.entries.put(date, new GratuityReportEntry(date));
        }

        this.entries.get(date).addGratuityAmount(amount);
    }

    public Collection<GratuityReportEntry> getEntries() {
        return this.entries.values();
    }

    public Money getTotalGratuityAmount() {
        Money result = Money.ZERO;

        for (final GratuityReportEntry each : this.entries.values()) {
            result = result.add(each.getGratuityAmount());
        }

        return result;
    }

}
