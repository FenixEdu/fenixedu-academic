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
/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.oldInquiries;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoInquiriesRoom;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class InquiriesRoom extends InquiriesRoom_Base {

    public InquiriesRoom() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected InquiriesRoom(InquiriesCourse inquiriesCourse, Space room, InfoInquiriesRoom infoInquiriesRoom) {
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
    public boolean hasBennu() {
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
