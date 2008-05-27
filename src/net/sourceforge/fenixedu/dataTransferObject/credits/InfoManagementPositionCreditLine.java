/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;

/**
 * @author jpvl
 */
public class InfoManagementPositionCreditLine extends InfoDatePeriodBaseCreditLine {

    private String position;

    private Double credits;

    /**
     * @return Returns the position.
     */
    public String getPosition() {
	return position;
    }

    /**
     * @param position
     *                The position to set.
     */
    public void setPosition(String position) {
	this.position = position;
    }

    public Double getCredits() {
	return credits;
    }

    public void setCredits(Double credits) {
	this.credits = credits;
    }

    public static InfoManagementPositionCreditLine newInfoFromDomain(ManagementPositionCreditLine managementPositionCreditLine) {
	InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(managementPositionCreditLine.getTeacher());

	InfoManagementPositionCreditLine infoCreditLine = new InfoManagementPositionCreditLine();
	infoCreditLine.setCredits(managementPositionCreditLine.getCredits());
	infoCreditLine.setEnd(managementPositionCreditLine.getEnd().toDateMidnight().toDate());
	infoCreditLine.setIdInternal(managementPositionCreditLine.getIdInternal());
	infoCreditLine.setPosition(managementPositionCreditLine.getPosition());
	infoCreditLine.setStart(managementPositionCreditLine.getStart().toDateMidnight().toDate());

	infoCreditLine.setInfoTeacher(infoTeacher);
	return infoCreditLine;
    }
}