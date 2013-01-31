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
