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
package org.fenixedu.academic.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money implements Comparable<Money>, Serializable {

    private static final BigDecimal CONVERSION_RATE_ESCUDOS_TO_EURO = BigDecimal.valueOf(200.482);

    private static final int EURO_SCALE = 2;

    public static final Money ZERO = new Money(0);

    private final BigDecimal amount;

    public Money(long amount) {
        this.amount = round(BigDecimal.valueOf(amount));
    }

    public Money(BigDecimal amount) {
        this.amount = round(amount);
    }

    public Money(String amount) {
        this.amount = round(new BigDecimal(amount));
    }

    public Money(Double amount) {
        this.amount = round(new BigDecimal(amount));
    }

    private BigDecimal round(BigDecimal amountToRound) {
        return amountToRound.setScale(EURO_SCALE, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getAmountAsString() {
        return toPlainString();
    }

    @Override
    public int compareTo(Money otherMoney) {
        return this.amount.compareTo(otherMoney.getAmount());
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Money) ? compareTo((Money) obj) == 0 : false;
    }

    @Override
    public int hashCode() {
        return this.amount.hashCode();
    }

    public Money add(Money money) {
        return new Money(this.amount.add(money.getAmount()));
    }

    public Money add(BigDecimal amount) {
        return new Money(this.amount.add(amount));
    }

    public Money subtract(Money money) {
        if (money != null) {
            return new Money(this.amount.subtract(money.getAmount()));
        } else {
            return this;
        }
    }

    public Money subtract(BigDecimal amount) {
        return new Money(this.amount.subtract(amount));
    }

    public Money multiply(Money money) {
        if (money != null) {
            return new Money(this.amount.multiply(money.getAmount()));
        } else {
            return this;
        }
    }

    public Money multiply(BigDecimal amount) {
        return new Money(this.amount.multiply(amount));
    }

    public Money multiply(final int value) {
        return multiply(BigDecimal.valueOf(value));
    }

    public Money divide(Money money) {
        if (money != null) {
            return divide(money.getAmount());
        } else {
            return this;
        }
    }

    public Money divide(BigDecimal amount) {
        return new Money(this.amount.divide(amount, 2, RoundingMode.HALF_EVEN));
    }

    public String toPlainString() {
        return this.amount.toPlainString();
    }

    @Override
    public String toString() {
        return this.amount.toString();
    }

    public static Money valueOf(long value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public boolean greaterThan(Money money) {
        return this.compareTo(money) > 0;
    }

    public boolean greaterOrEqualThan(Money money) {
        return this.compareTo(money) >= 0;
    }

    public boolean lessThan(Money money) {
        return this.compareTo(money) < 0;
    }

    public boolean lessOrEqualThan(Money money) {
        return this.compareTo(money) <= 0;
    }

    public boolean isPositive() {
        return this.amount.signum() == 1;
    }

    public boolean isZero() {
        return this.amount.signum() == 0;
    }

    public boolean isNegative() {
        return this.amount.signum() == -1;
    }

    public Money negate() {
        return new Money(this.amount.negate());
    }

    public long longValue() {
        return this.amount.longValueExact();
    }

    public Money abs() {
        return new Money(this.amount.abs());
    }

    public Money escudosToEuros() {
        return new Money(this.amount.divide(CONVERSION_RATE_ESCUDOS_TO_EURO, 2, RoundingMode.HALF_UP));
    }

    static public Money min(final Money value1, final Money value2) {
        return value1.lessThan(value2) ? value1 : value2;
    }

    static public Money max(final Money value1, final Money value2) {
        return value1.lessThan(value2) ? value2 : value1;
    }
}
