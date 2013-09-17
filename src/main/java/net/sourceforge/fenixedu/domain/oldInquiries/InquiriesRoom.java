/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.oldInquiries;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoInquiriesRoom;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class InquiriesRoom extends InquiriesRoom_Base {

    public InquiriesRoom() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    protected InquiriesRoom(InquiriesCourse inquiriesCourse, AllocatableSpace room, InfoInquiriesRoom infoInquiriesRoom) {
        this();
        if ((inquiriesCourse == null) || (room == null)) {
            throw new DomainException("The inquiriesCourse and room should not be null!");
        }

        this.setInquiriesCourse(inquiriesCourse);
        this.setRoom(room);
        this.setBasicProperties(infoInquiriesRoom);

    }

    private void setBasicProperties(InfoInquiriesRoom infoInquiriesRoom) {
        this.setEnvironmentalConditions(infoInquiriesRoom.getEnvironmentalConditions());
        this.setEquipmentQuality(infoInquiriesRoom.getEquipmentQuality());
        this.setSpaceAdequation(infoInquiriesRoom.getSpaceAdequation());

    }
    @Deprecated
    public boolean hasInquiriesCourse() {
        return getInquiriesCourse() != null;
    }

    @Deprecated
    public boolean hasRoom() {
        return getRoom() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEquipmentQuality() {
        return getEquipmentQuality() != null;
    }

    @Deprecated
    public boolean hasEnvironmentalConditions() {
        return getEnvironmentalConditions() != null;
    }

    @Deprecated
    public boolean hasSpaceAdequation() {
        return getSpaceAdequation() != null;
    }

}
