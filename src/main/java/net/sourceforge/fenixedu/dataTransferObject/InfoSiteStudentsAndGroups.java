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
 * Created on 8/Jan/2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;
import java.util.ListIterator;

/**
 * @author joaosa & rmalo
 * 
 */
public class InfoSiteStudentsAndGroups extends DataTranferObject implements ISiteComponent {

    private List infoSiteStudentsAndGroupsList;
    private InfoShift infoShift;
    private InfoGrouping infoGrouping;

    /**
     * @return
     */
    public List getInfoSiteStudentsAndGroupsList() {
        return infoSiteStudentsAndGroupsList;
    }

    /**
     * @param list
     */
    public void setInfoSiteStudentsAndGroupsList(List infoSiteStudentsAndGroupsList) {
        this.infoSiteStudentsAndGroupsList = infoSiteStudentsAndGroupsList;
    }

    /**
     * @return
     */
    public InfoShift getInfoShift() {
        return infoShift;
    }

    /**
     * @param list
     */
    public void setInfoShift(InfoShift infoShift) {
        this.infoShift = infoShift;
    }

    @Override
    public boolean equals(Object objectToCompare) {
        boolean result = true;

        if (objectToCompare instanceof InfoSiteStudentsAndGroups) {
            result = true;
        }

        if (((InfoSiteStudentsAndGroups) objectToCompare).getInfoSiteStudentsAndGroupsList() == null
                && this.getInfoSiteStudentsAndGroupsList() == null) {
            return true;
        }

        if (((InfoSiteStudentsAndGroups) objectToCompare).getInfoSiteStudentsAndGroupsList() == null
                || this.getInfoSiteStudentsAndGroupsList() == null
                || ((InfoSiteStudentsAndGroups) objectToCompare).getInfoSiteStudentsAndGroupsList().size() != this
                        .getInfoSiteStudentsAndGroupsList().size()) {

            return false;
        }

        ListIterator iter1 = ((InfoSiteStudentsAndGroups) objectToCompare).getInfoSiteStudentsAndGroupsList().listIterator();
        ListIterator iter2 = this.getInfoSiteStudentsAndGroupsList().listIterator();
        while (result && iter1.hasNext()) {
            InfoSiteStudentAndGroup infoSiteStudentAndGroup1 = (InfoSiteStudentAndGroup) iter1.next();
            InfoSiteStudentAndGroup infoSiteStudentAndGroup2 = (InfoSiteStudentAndGroup) iter2.next();

            if (!infoSiteStudentAndGroup1.equals(infoSiteStudentAndGroup2)) {
                result = false;
            }
        }
        return result;
    }

    public InfoGrouping getInfoGrouping() {
        return infoGrouping;
    }

    public void setInfoGrouping(InfoGrouping infoGrouping) {
        this.infoGrouping = infoGrouping;
    }
}