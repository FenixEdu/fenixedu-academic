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
 * Created on 4/Ago/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;
import java.util.ListIterator;

/**
 * @author asnr and scpo
 * 
 */

public class InfoSiteGroupsByShift extends DataTranferObject implements ISiteComponent {

    private List infoSiteStudentGroupsList;

    private InfoSiteShift infoSiteShift;

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String result = "[INFO_SITE_GROUPS_BY_SHIFT";
        result += ", infoStudentGroupsList=" + getInfoSiteStudentGroupsList();
        result += ", infoSiteShift=" + getInfoSiteShift();

        result += "]";
        return result;
    }

    @Override
    public boolean equals(Object objectToCompare) {
        boolean result = false;
        if (objectToCompare instanceof InfoSiteGroupsByShift) {

            result = (this.getInfoSiteShift().equals(((InfoSiteGroupsByShift) objectToCompare).getInfoSiteShift()));
        }

        if (((InfoSiteGroupsByShift) objectToCompare).getInfoSiteStudentGroupsList() == null
                && this.getInfoSiteStudentGroupsList() == null && result == true) {
            return true;
        }
        if (((InfoSiteGroupsByShift) objectToCompare).getInfoSiteStudentGroupsList() == null
                || this.getInfoSiteStudentGroupsList() == null
                || ((InfoSiteGroupsByShift) objectToCompare).getInfoSiteStudentGroupsList().size() != this
                        .getInfoSiteStudentGroupsList().size()) {
            return false;
        }

        ListIterator iter1 = ((InfoSiteGroupsByShift) objectToCompare).getInfoSiteStudentGroupsList().listIterator();
        ListIterator iter2 = this.getInfoSiteStudentGroupsList().listIterator();
        while (result && iter1.hasNext()) {
            InfoSiteStudentGroup infoSiteStudentGroup1 = (InfoSiteStudentGroup) iter1.next();
            InfoSiteStudentGroup infoSiteStudentGroup2 = (InfoSiteStudentGroup) iter2.next();
            if (!infoSiteStudentGroup1.equals(infoSiteStudentGroup2)) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @return
     */
    public List getInfoSiteStudentGroupsList() {
        return infoSiteStudentGroupsList;
    }

    /**
     * @param list
     */
    public void setInfoSiteStudentGroupsList(List infoStudentGroupsList) {
        this.infoSiteStudentGroupsList = infoStudentGroupsList;
    }

    /**
     * @return
     */
    public InfoSiteShift getInfoSiteShift() {
        return infoSiteShift;
    }

    /**
     * @param list
     */
    public void setInfoSiteShift(InfoSiteShift infoSiteShift) {
        this.infoSiteShift = infoSiteShift;
    }

}