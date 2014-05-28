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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Fernanda & Tânia Pousão e Ângela
 * 
 * 
 */
public class InfoSiteStudents extends DataTranferObject implements ISiteComponent {

    // private InfoCurricularCourseScope infoCurricularCourseScope;
    private InfoCurricularCourse infoCurricularCourse;

    private List students;

    @Override
    public boolean equals(Object objectToCompare) {
        boolean result = false;
        // if (objectToCompare instanceof InfoSiteStudents
        // && (((((InfoSiteStudents)
        // objectToCompare).getInfoCurricularCourseScope() != null
        // && this.getInfoCurricularCourseScope() != null
        // && ((InfoSiteStudents)
        // objectToCompare).getInfoCurricularCourseScope().equals(this.
        // getInfoCurricularCourseScope()))
        // || ((InfoSiteStudents)
        // objectToCompare).getInfoCurricularCourseScope() == null
        // && this.getInfoCurricularCourseScope() == null))) {
        // result = true;
        // }

        if (objectToCompare instanceof InfoSiteStudents
                && (((((InfoSiteStudents) objectToCompare).getInfoCurricularCourse() != null
                        && this.getInfoCurricularCourse() != null && ((InfoSiteStudents) objectToCompare)
                        .getInfoCurricularCourse().equals(this.getInfoCurricularCourse())) || ((InfoSiteStudents) objectToCompare)
                        .getInfoCurricularCourse() == null && this.getInfoCurricularCourse() == null))) {
            result = true;
        }

        if (((InfoSiteStudents) objectToCompare).getStudents() == null && this.getStudents() == null) {
            return true;
        }
        if (((InfoSiteStudents) objectToCompare).getStudents() == null || this.getStudents() == null
                || ((InfoSiteStudents) objectToCompare).getStudents().size() != this.getStudents().size()) {
            return false;
        }
        ListIterator iter1 = ((InfoSiteStudents) objectToCompare).getStudents().listIterator();
        ListIterator iter2 = this.getStudents().listIterator();
        while (result && iter1.hasNext()) {
            InfoStudent infoStudent1 = (InfoStudent) iter1.next();
            InfoStudent infoStudent2 = (InfoStudent) iter2.next();
            if (!infoStudent1.equals(infoStudent2)) {
                result = false;
            }
        }

        return result;
    }

    /**
     * @return
     */
    public List getStudents() {
        return students;
    }

    /**
     * @param list
     */
    public void setStudents(List list) {
        students = list;
    }

    /**
     * @return
     */
    // public InfoCurricularCourseScope getInfoCurricularCourseScope() {
    // return infoCurricularCourseScope;
    // }
    /**
     * @param scope
     */
    // public void setInfoCurricularCourseScope(InfoCurricularCourseScope scope)
    // {
    // infoCurricularCourseScope = scope;
    // }
    /**
     * @return Returns the infoCurricularCourse.
     */
    public InfoCurricularCourse getInfoCurricularCourse() {
        return infoCurricularCourse;
    }

    /**
     * @param infoCurricularCourse
     *            The infoCurricularCourse to set.
     */
    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
        this.infoCurricularCourse = infoCurricularCourse;
    }

}