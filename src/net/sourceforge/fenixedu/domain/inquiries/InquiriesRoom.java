/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class InquiriesRoom extends InquiriesRoom_Base {

    public InquiriesRoom() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected InquiriesRoom(InquiriesCourse inquiriesCourse, OldRoom room,
	    InfoInquiriesRoom infoInquiriesRoom) {
	this();
	if ((inquiriesCourse == null) || (room == null))
	    throw new DomainException("The inquiriesCourse and room should not be null!");

	this.setInquiriesCourse(inquiriesCourse);
	this.setRoom(room);
	this.setBasicProperties(infoInquiriesRoom);

    }

    private void setBasicProperties(InfoInquiriesRoom infoInquiriesRoom) {
	this.setEnvironmentalConditions(infoInquiriesRoom.getEnvironmentalConditions());
	this.setEquipmentQuality(infoInquiriesRoom.getEquipmentQuality());
	this.setSpaceAdequation(infoInquiriesRoom.getSpaceAdequation());

    }
}
