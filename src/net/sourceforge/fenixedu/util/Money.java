package net.sourceforge.fenixedu.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money implements Comparable<Money>, Serializable {

    private static final int EURO_SCALE = 2;

    public static final Money ZERO = new Money(0);

    private BigDecimal amount;

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
	return new Money(this.amount.subtract(money.getAmount()));
    }

    public Money subtract(BigDecimal amount) {
	return new Money(this.amount.subtract(amount));
    }

    public Money multiply(Money money) {
	return new Money(this.amount.multiply(money.getAmount()));
    }

    public Money multiply(BigDecimal amount) {
	return new Money(this.amount.multiply(amount));
    }

    public Money divide(Money money) {
	return new Money(this.amount.divide(money.getAmount()));
    }

    public Money divide(BigDecimal amount) {
	return new Money(this.amount.divide(amount));
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
    
}
