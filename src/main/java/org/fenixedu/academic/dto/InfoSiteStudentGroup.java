/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 4/Ago/2003
 *
 */
package org.fenixedu.academic.dto;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author asnr and scpo
 * 
 */
public class InfoSiteStudentGroup extends DataTranferObject {

    public static final Comparator<InfoSiteStudentGroup> COMPARATOR_BY_NUMBER = new Comparator<InfoSiteStudentGroup>() {

        @Override
        public int compare(InfoSiteStudentGroup o1, InfoSiteStudentGroup o2) {
            final InfoStudentGroup g1 = o1.getInfoStudentGroup();
            final InfoStudentGroup g2 = o2.getInfoStudentGroup();
            return g1.getGroupNumber().compareTo(g2.getGroupNumber());
        }

    };

    // Collections.sort(infoSiteStudentGroupsList, new
    // BeanComparator("infoStudentGroup.groupNumber"));

    private List infoSiteStudentInformationList;

    private InfoStudentGroup infoStudentGroup;

    private Object nrOfElements;

    /**
     * @return
     */
    public InfoStudentGroup getInfoStudentGroup() {
        return infoStudentGroup;
    }

    /**
     * @param infoStudentGroup
     */
    public void setInfoStudentGroup(InfoStudentGroup infoStudentGroup) {
        this.infoStudentGroup = infoStudentGroup;
    }

    /**
     * @return
     */
    public Object getNrOfElements() {
        return nrOfElements;
    }

    /**
     * @param nrOfElements
     */
    public void setNrOfElements(Object nrOfElements) {
        this.nrOfElements = nrOfElements;
    }

    /**
     * @return
     */
    public List getInfoSiteStudentInformationList() {
        return infoSiteStudentInformationList;
    }

    /**
     * @param infoSiteStudentInformationList
     */
    public void setInfoSiteStudentInformationList(List infoSiteStudentInformationList) {
        this.infoSiteStudentInformationList = infoSiteStudentInformationList;
    }

    @Override
    public boolean equals(Object objectToCompare) {
        boolean result = false;

        if (objectToCompare instanceof InfoSiteStudentGroup) {
            if (this.getInfoStudentGroup() == null && ((InfoSiteStudentGroup) objectToCompare).getInfoStudentGroup() == null) {
                result = true;
            } else {
                result = (this.getInfoStudentGroup().equals(((InfoSiteStudentGroup) objectToCompare).getInfoStudentGroup()));
            }

        }

        if (((InfoSiteStudentGroup) objectToCompare).getInfoSiteStudentInformationList() == null
                && this.getInfoSiteStudentInformationList() == null && result) {

            return true;
        }

        if (((InfoSiteStudentGroup) objectToCompare).getInfoSiteStudentInformationList() == null
                || this.getInfoSiteStudentInformationList() == null
                || ((InfoSiteStudentGroup) objectToCompare).getInfoSiteStudentInformationList().size() != this
                        .getInfoSiteStudentInformationList().size()) {

            return false;
        }

        ListIterator iter1 = ((InfoSiteStudentGroup) objectToCompare).getInfoSiteStudentInformationList().listIterator();
        ListIterator iter2 = this.getInfoSiteStudentInformationList().listIterator();
        while (result && iter1.hasNext()) {
            InfoSiteStudentInformation infoSiteStudentInformation1 = (InfoSiteStudentInformation) iter1.next();
            InfoSiteStudentInformation infoSiteStudentInformation2 = (InfoSiteStudentInformation) iter2.next();

            if (!infoSiteStudentInformation1.equals(infoSiteStudentInformation2)) {

                result = false;
            }
        }
        return result;
    }
}