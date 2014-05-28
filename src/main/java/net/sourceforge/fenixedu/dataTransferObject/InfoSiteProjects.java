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

public class InfoSiteProjects extends DataTranferObject implements ISiteComponent {

    private InfoExecutionCourse infoExecutionCourse;

    private List infoGroupPropertiesList;

    public List getInfoGroupPropertiesList() {
        return infoGroupPropertiesList;
    }

    public void setInfoGroupPropertiesList(List infoGroupPropertiesList) {
        this.infoGroupPropertiesList = infoGroupPropertiesList;
    }

    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    @Override
    public boolean equals(Object objectToCompare) {
        boolean result = false;
        if (objectToCompare instanceof InfoSiteProjects) {
            result = true;
        }

        if (((InfoSiteProjects) objectToCompare).getInfoExecutionCourse() == null && (this.infoExecutionCourse != null)
                || ((InfoSiteProjects) objectToCompare).getInfoExecutionCourse() != null && (this.infoExecutionCourse == null)) {
            return false;
        }

        if (!(((InfoSiteProjects) objectToCompare).getInfoExecutionCourse() == null && (this.infoExecutionCourse == null))) {
            if (!(((InfoSiteProjects) objectToCompare).getInfoExecutionCourse()).equals(this.infoExecutionCourse)) {
                return false;
            }
        }

        if (((InfoSiteProjects) objectToCompare).getInfoGroupPropertiesList() == null
                && this.getInfoGroupPropertiesList() == null) {
            return true;
        }

        if (((InfoSiteProjects) objectToCompare).getInfoGroupPropertiesList() == null
                || this.getInfoGroupPropertiesList() == null
                || ((InfoSiteProjects) objectToCompare).getInfoGroupPropertiesList().size() != this.getInfoGroupPropertiesList()
                        .size()) {
            return false;
        }

        ListIterator iter1 = ((InfoSiteProjects) objectToCompare).getInfoGroupPropertiesList().listIterator();
        ListIterator iter2 = this.getInfoGroupPropertiesList().listIterator();
        while (result && iter1.hasNext()) {

            InfoGrouping groupProperties1 = (InfoGrouping) iter1.next();
            InfoGrouping groupProperties2 = (InfoGrouping) iter2.next();
            if (!groupProperties1.equals(groupProperties2)) {

                result = false;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        String result = "[InfoSiteProjects: ";
        result += "infoGroupPropertiesList - " + this.getInfoGroupPropertiesList() + "]";
        return result;
    }

}
