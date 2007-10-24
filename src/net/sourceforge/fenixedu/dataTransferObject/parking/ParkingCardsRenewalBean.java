package net.sourceforge.fenixedu.dataTransferObject.parking;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;

import org.joda.time.YearMonthDay;

public class ParkingCardsRenewalBean implements Serializable {

    private DomainReference<ParkingGroup> parkingGroup;

    private DomainListReference<ParkingParty> notRenewedParkingParties = new DomainListReference<ParkingParty>();

    private YearMonthDay actualEndDate;

    private YearMonthDay renewalEndDate;

    private int groupMembersCount;
    
    private boolean processed = Boolean.FALSE;

    public ParkingCardsRenewalBean() {
    }

    public ParkingGroup getParkingGroup() {
	return parkingGroup != null ? parkingGroup.getObject() : null;
    }

    public void setParkingGroup(ParkingGroup parkingGroup) {
	this.parkingGroup = parkingGroup != null ? new DomainReference<ParkingGroup>(parkingGroup)
		: null;
    }

    public YearMonthDay getActualEndDate() {
	return actualEndDate;
    }

    public void setActualEndDate(YearMonthDay actualEndDate) {
	this.actualEndDate = actualEndDate;
    }

    public YearMonthDay getRenewalEndDate() {
	return renewalEndDate;
    }

    public void setRenewalEndDate(YearMonthDay renewalEndDate) {
	this.renewalEndDate = renewalEndDate;
    }

    public DomainListReference<ParkingParty> getNotRenewedParkingParties() {
	return notRenewedParkingParties;
    }

    public void setNotRenewedParkingParties(DomainListReference<ParkingParty> notRenewedParkingParties) {
	this.notRenewedParkingParties = notRenewedParkingParties;
    }

    public int getGroupMembersCount() {
	return groupMembersCount;
    }

    public void setGroupMembersCount(int groupMembersCount) {
	this.groupMembersCount = groupMembersCount;
    }

    public boolean getProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
