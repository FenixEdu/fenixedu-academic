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
package net.sourceforge.fenixedu.domain.phd.debts;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

public class PhdGratuityPaymentPeriod extends PhdGratuityPaymentPeriod_Base {

    public PhdGratuityPaymentPeriod(int dayStart, int monthStart, int dayEnd, int monthEnd, int dayLastPayment,
            int monthLastPayment) {
        super();
        init(new Partial().with(DateTimeFieldType.monthOfYear(), monthStart).with(DateTimeFieldType.dayOfYear(), dayStart),
                new Partial().with(DateTimeFieldType.monthOfYear(), monthEnd).with(DateTimeFieldType.dayOfYear(), dayEnd),
                new Partial().with(DateTimeFieldType.monthOfYear(), monthLastPayment).with(DateTimeFieldType.dayOfYear(),
                        dayLastPayment));
    }

    protected void init(Partial start, Partial end, Partial lastPaymentDay) {
        setStart(start);
        setEnd(end);
        setLastPayment(lastPaymentDay);
    }

    public PhdGratuityPaymentPeriod(Partial start, Partial end, Partial lastPaymentDay) {
        super();
        init(start, end, lastPaymentDay);
    }

    public int getMonthStart() {
        return getStart().get(DateTimeFieldType.monthOfYear());
    }

    public int getDayStart() {
        return getStart().get(DateTimeFieldType.dayOfMonth());
    }

    public int getMonthEnd() {
        return getEnd().get(DateTimeFieldType.monthOfYear());
    }

    public int getDayEnd() {
        return getEnd().get(DateTimeFieldType.dayOfMonth());
    }

    public int getMonthLastPayment() {
        return getLastPayment().get(DateTimeFieldType.monthOfYear());
    }

    public int getDayLastPayment() {
        return getLastPayment().get(DateTimeFieldType.dayOfMonth());
    }

    public boolean contains(LocalDate date) {
        LocalDate start = new LocalDate(date.getYear(), getMonthStart(), getDayStart());
        LocalDate end = new LocalDate(date.getYear(), getMonthEnd(), getDayEnd());

        if ((date.equals(start) || date.isAfter(start)) && (date.equals(end) || date.isBefore(end))) {
            return true;
        } else {
            return false;
        }
    }

    public Money fine(double fineRate, Money amount, DateTime when) {
        LocalDate whenPaying = new LocalDate(when.getYear(), when.monthOfYear().get(), when.dayOfMonth().get());
        LocalDate lastPaymentDay = new LocalDate(when.getYear(), getMonthLastPayment(), getDayLastPayment());

        if (whenPaying.isAfter(lastPaymentDay)) {
            int monthsOut = when.getMonthOfYear() - lastPaymentDay.getMonthOfYear();

            // if is in the same month, and a day has passed, at least it
            // counts for one month
            if (monthsOut == 0) {
                monthsOut = 1;
            }
            return new Money(amount.getAmount().multiply(new BigDecimal(fineRate * monthsOut)));
        } else {
            return new Money(0);
        }
    }

    /*
     * @Atomic public static final PhdGratuityPaymentPeriod
     * makeNewFirstStandardPeriod() { return new PhdGratuityPaymentPeriod(1, 1,
     * 30, 6, 31, 8); }
     * 
     * @Atomic public static final PhdGratuityPaymentPeriod
     * makeNewSecondStandardPeriod() { return new PhdGratuityPaymentPeriod(1, 7,
     * 31, 12, 28, 2); }
     */

    public void delete() {
        setRootDomainObject(null);
        setPostingRule(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasEnd() {
        return getEnd() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPostingRule() {
        return getPostingRule() != null;
    }

    @Deprecated
    public boolean hasStart() {
        return getStart() != null;
    }

    @Deprecated
    public boolean hasLastPayment() {
        return getLastPayment() != null;
    }

}
