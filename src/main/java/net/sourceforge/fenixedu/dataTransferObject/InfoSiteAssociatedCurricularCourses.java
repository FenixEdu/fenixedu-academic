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
 * Created on 5/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;
import java.util.ListIterator;

/**
 * @author João Mota
 * 
 * 
 */
public class InfoSiteAssociatedCurricularCourses extends DataTranferObject implements ISiteComponent {

    private List associatedCurricularCourses;

    @Override
    public boolean equals(Object objectToCompare) {
        boolean result = false;
        if (objectToCompare instanceof InfoSiteAssociatedCurricularCourses) {
            result = true;
        }

        if (((InfoSiteAssociatedCurricularCourses) objectToCompare).getAssociatedCurricularCourses() == null
                && this.getAssociatedCurricularCourses() == null) {
            return true;
        }
        if (((InfoSiteAssociatedCurricularCourses) objectToCompare).getAssociatedCurricularCourses() == null
                || this.getAssociatedCurricularCourses() == null
                || ((InfoSiteAssociatedCurricularCourses) objectToCompare).getAssociatedCurricularCourses().size() != this
                        .getAssociatedCurricularCourses().size()) {
            return false;
        }
        ListIterator iter1 =
                ((InfoSiteAssociatedCurricularCourses) objectToCompare).getAssociatedCurricularCourses().listIterator();
        ListIterator iter2 = this.getAssociatedCurricularCourses().listIterator();
        while (result && iter1.hasNext()) {
            InfoCurricularCourse infoCurricularCourse1 = (InfoCurricularCourse) iter1.next();
            InfoCurricularCourse infoCurricularCourse2 = (InfoCurricularCourse) iter2.next();
            if (!infoCurricularCourse1.equals(infoCurricularCourse2)) {
                result = false;
            }
        }

        return result;
    }

    /**
     * @return
     */
    public List getAssociatedCurricularCourses() {
        return associatedCurricularCourses;
    }

    /**
     * @param list
     */
    public void setAssociatedCurricularCourses(List list) {
        associatedCurricularCourses = list;
    }

}