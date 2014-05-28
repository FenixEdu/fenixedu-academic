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

import java.util.Comparator;
import java.util.List;

/**
 * @author joaosa & rmalo
 * 
 */
public class InfoSiteStudentsAndShiftByStudentGroup extends DataTranferObject implements ISiteComponent {

    public static final Comparator<InfoSiteStudentsAndShiftByStudentGroup> COMPARATOR_BY_NUMBER =
            new Comparator<InfoSiteStudentsAndShiftByStudentGroup>() {

                @Override
                public int compare(InfoSiteStudentsAndShiftByStudentGroup o1, InfoSiteStudentsAndShiftByStudentGroup o2) {
                    final InfoStudentGroup g1 = o1.getInfoStudentGroup();
                    final InfoStudentGroup g2 = o2.getInfoStudentGroup();
                    return g1.getGroupNumber().compareTo(g2.getGroupNumber());
                }

            };

    private List infoSiteStudentInformationList;
    private InfoStudentGroup infoStudentGroup;
    private InfoShift infoShift;

    /**
     * @return
     */
    public List getInfoSiteStudentInformationList() {
        return infoSiteStudentInformationList;
    }

    /**
     * @param list
     */
    public void setInfoSiteStudentInformationList(List infoSiteStudentInformationList) {
        this.infoSiteStudentInformationList = infoSiteStudentInformationList;
    }

    /**
     * @return
     */
    public InfoStudentGroup getInfoStudentGroup() {
        return infoStudentGroup;
    }

    /**
     * @param list
     */
    public void setInfoStudentGroup(InfoStudentGroup infoStudentGroup) {
        this.infoStudentGroup = infoStudentGroup;
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

}
