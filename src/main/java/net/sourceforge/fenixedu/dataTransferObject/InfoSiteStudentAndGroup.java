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

/**
 * @author joaosa & rmalo
 * 
 */
public class InfoSiteStudentAndGroup extends DataTranferObject implements ISiteComponent {

    public static final Comparator<InfoSiteStudentAndGroup> COMPARATOR_BY_NUMBER = new Comparator<InfoSiteStudentAndGroup>() {

        @Override
        public int compare(InfoSiteStudentAndGroup o1, InfoSiteStudentAndGroup o2) {
            return InfoSiteStudentInformation.COMPARATOR_BY_NUMBER.compare(o1.getInfoSiteStudentInformation(),
                    o2.getInfoSiteStudentInformation());
        }

    };

    // Collections.sort(infoSiteStudentsAndGroupsList, new
    // BeanComparator("infoSiteStudentInformation.number"));

    private InfoSiteStudentInformation infoSiteStudentInformation;
    private InfoStudentGroup infoStudentGroup;

    /**
     * @return InfoSiteStudentInformation
     */
    public InfoSiteStudentInformation getInfoSiteStudentInformation() {
        return infoSiteStudentInformation;
    }

    /**
     * @param InfoSiteStudentInformation
     */
    public void setInfoSiteStudentInformation(InfoSiteStudentInformation infoSiteStudentInformation) {
        this.infoSiteStudentInformation = infoSiteStudentInformation;
    }

    /**
     * @return InfoStudentGroup
     */
    public InfoStudentGroup getInfoStudentGroup() {
        return infoStudentGroup;
    }

    /**
     * @param InfoStudentGroup
     */
    public void setInfoStudentGroup(InfoStudentGroup infoStudentGroup) {
        this.infoStudentGroup = infoStudentGroup;
    }
}