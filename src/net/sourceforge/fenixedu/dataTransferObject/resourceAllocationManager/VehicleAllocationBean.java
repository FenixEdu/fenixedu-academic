package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.resource.Vehicle;

import org.joda.time.DateTime;

public class VehicleAllocationBean implements Serializable {

    private String reason;

    private DateTime beginDateTime;

    private DateTime endDateTime;

    private Party partyReference;

    private Vehicle vehicleReference;

    private BigDecimal distance;

    private BigDecimal amountCharged;

    public VehicleAllocationBean() {

    }

    public BigDecimal getAmountCharged() {
        return amountCharged;
    }

    public void setAmountCharged(BigDecimal amountCharged) {
        this.amountCharged = amountCharged;
    }

    public Party getRequestor() {
        return this.partyReference;
    }

    public void setRequestor(Party requestor) {
        this.partyReference = requestor;
    }

    public Vehicle getVehicle() {
        return this.vehicleReference;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicleReference = vehicle;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DateTime getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(DateTime beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }
}
