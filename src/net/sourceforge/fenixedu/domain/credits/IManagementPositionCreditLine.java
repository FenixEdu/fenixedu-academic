/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.domain.credits;

/**
 * @author jpvl
 */
public interface IManagementPositionCreditLine extends IDatePeriodBasedCreditLine {
    public abstract String getPosition();

    public abstract void setPosition(String position);

    public abstract Double getCredits();

    public abstract void setCredits(Double credits);
}