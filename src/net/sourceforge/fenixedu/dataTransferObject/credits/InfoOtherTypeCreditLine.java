/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.credits.IOtherTypeCreditLine;

/**
 * @author jpvl
 */
public class InfoOtherTypeCreditLine extends InfoCreditLine {
    private InfoExecutionPeriod infoExecutionPeriod;

    private String reason;

    private Double credits;

    /**
     * @return Returns the infoExecutionPeriod.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }

    /**
     * @param infoExecutionPeriod
     *            The infoExecutionPeriod to set.
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
     * @return Returns the reason.
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason
     *            The reason to set.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return Returns the credits.
     */
    public Double getCredits() {
        return credits;
    }

    /**
     * @param credits
     *            The credits to set.
     */
    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public static InfoOtherTypeCreditLine newInfoFromDomain(IOtherTypeCreditLine otherTypeCreditLine) {
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(otherTypeCreditLine.getTeacher());
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(otherTypeCreditLine.getExecutionPeriod());

        InfoOtherTypeCreditLine infoOtherTypeCreditLine = new InfoOtherTypeCreditLine();
        infoOtherTypeCreditLine.setCredits(otherTypeCreditLine.getCredits());
        infoOtherTypeCreditLine.setIdInternal(otherTypeCreditLine.getIdInternal());
        infoOtherTypeCreditLine.setReason(otherTypeCreditLine.getReason());

        infoOtherTypeCreditLine.setInfoTeacher(infoTeacher);
        infoOtherTypeCreditLine.setInfoExecutionPeriod(infoExecutionPeriod);
        return infoOtherTypeCreditLine;
    }

}