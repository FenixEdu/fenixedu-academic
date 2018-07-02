package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public abstract class AccountingEntry {

    interface View {
        interface Simple {

        }
        interface Detailed {

        }
    }

    @JsonView(AccountingEntry.View.Simple.class)
    private final String id;

    @JsonView(AccountingEntry.View.Simple.class)
    private final DateTime created;

    @JsonView(AccountingEntry.View.Simple.class)
    private final LocalDate date;

    @JsonView(AccountingEntry.View.Simple.class)
    private final String description;

    @JsonView(AccountingEntry.View.Simple.class)
    private final BigDecimal amount;

    public AccountingEntry(String id, DateTime created, LocalDate date, String description, BigDecimal amount) {
        this.id = id;
        this.created = created;
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public DateTime getCreated() {
        return created;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalizedString getTypeDescription() {
        return BundleUtil.getLocalizedString(Bundle.APPLICATION, "label.accounting.entry." + getClass().getSimpleName());
    }
}
