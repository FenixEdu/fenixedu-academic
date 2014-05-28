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
 * Created on 23/Setembro/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;
import java.util.ListIterator;

/**
 * @author asnr and scpo
 * 
 */
public class InfoSiteShiftsAndGroups extends DataTranferObject implements ISiteComponent {

    private List infoSiteGroupsByShiftList;
    private InfoGrouping infoGrouping;
    private Integer numberOfStudentsOutsideGrouping;
    private Integer numberOfStudentsInsideGrouping;

    /**
     * @return
     */
    public List getInfoSiteGroupsByShiftList() {
        return infoSiteGroupsByShiftList;
    }

    /**
     * @param list
     */
    public void setInfoSiteGroupsByShiftList(List infoSiteGroupsByShiftList) {
        this.infoSiteGroupsByShiftList = infoSiteGroupsByShiftList;
    }

    @Override
    public boolean equals(Object objectToCompare) {
        boolean result = true;

        if (objectToCompare instanceof InfoSiteShiftsAndGroups) {
            result = true;
        }

        if (((InfoSiteShiftsAndGroups) objectToCompare).getInfoSiteGroupsByShiftList() == null
                && this.getInfoSiteGroupsByShiftList() == null) {
            return true;
        }

        if (((InfoSiteShiftsAndGroups) objectToCompare).getInfoSiteGroupsByShiftList() == null
                || this.getInfoSiteGroupsByShiftList() == null
                || ((InfoSiteShiftsAndGroups) objectToCompare).getInfoSiteGroupsByShiftList().size() != this
                        .getInfoSiteGroupsByShiftList().size()) {

            return false;
        }

        ListIterator iter1 = ((InfoSiteShiftsAndGroups) objectToCompare).getInfoSiteGroupsByShiftList().listIterator();
        ListIterator iter2 = this.getInfoSiteGroupsByShiftList().listIterator();
        while (result && iter1.hasNext()) {
            InfoSiteGroupsByShift infoSiteGroupsByShift1 = (InfoSiteGroupsByShift) iter1.next();
            InfoSiteGroupsByShift infoSiteGroupsByShift2 = (InfoSiteGroupsByShift) iter2.next();

            if (!infoSiteGroupsByShift1.equals(infoSiteGroupsByShift2)) {
                result = false;
            }
        }
        return result;
    }

    public void setInfoGrouping(InfoGrouping infoGrouping) {
        this.infoGrouping = infoGrouping;
    }

    public InfoGrouping getInfoGrouping() {
        return infoGrouping;
    }

    public Integer getNumberOfStudentsOutsideAttendsSet() {
        return numberOfStudentsOutsideGrouping;
    }

    public void setNumberOfStudentsOutsideAttendsSet(Integer numberOfStudentsOutsideAttendsSet) {
        this.numberOfStudentsOutsideGrouping = numberOfStudentsOutsideAttendsSet;
    }

    public Integer getNumberOfStudentsInsideAttendsSet() {
        return numberOfStudentsInsideGrouping;
    }

    public void setNumberOfStudentsInsideAttendsSet(Integer numberOfStudentsInsideAttendsSet) {
        this.numberOfStudentsInsideGrouping = numberOfStudentsInsideAttendsSet;
    }
}
