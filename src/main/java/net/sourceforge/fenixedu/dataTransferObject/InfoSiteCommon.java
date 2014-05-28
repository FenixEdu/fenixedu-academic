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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author João Mota
 * 
 * 
 */
public class InfoSiteCommon extends DataTranferObject implements ISiteComponent {

    private String title;

    private String mail;

    private InfoExecutionCourse executionCourse;

    private List sections;

    private List associatedDegrees;

    private List associatedDegreesByDegree;

    // in reality the associatedDegrees list is a list of curricular courses

    @Override
    public boolean equals(Object objectToCompare) {
        boolean result = false;
        if (objectToCompare instanceof InfoSiteCommon
                && (((((InfoSiteCommon) objectToCompare).getTitle() != null && this.getTitle() != null && ((InfoSiteCommon) objectToCompare)
                        .getTitle().equals(this.getTitle())) || ((InfoSiteCommon) objectToCompare).getTitle() == null
                        && this.getTitle() == null))
                && (((((InfoSiteCommon) objectToCompare).getMail() != null && this.getMail() != null && ((InfoSiteCommon) objectToCompare)
                        .getMail().equals(this.getMail())) || ((InfoSiteCommon) objectToCompare).getMail() == null
                        && this.getMail() == null))
                && (((((InfoSiteCommon) objectToCompare).getExecutionCourse() != null && this.getExecutionCourse() != null && ((InfoSiteCommon) objectToCompare)
                        .getExecutionCourse().equals(this.getExecutionCourse())) || ((InfoSiteCommon) objectToCompare)
                        .getExecutionCourse() == null && this.getExecutionCourse() == null))) {

            result = true;
        }

        if (((InfoSiteCommon) objectToCompare).getSections() == null && this.getSections() == null && result == true) {
            return true;
        }
        if (((InfoSiteCommon) objectToCompare).getSections() == null || this.getSections() == null
                || ((InfoSiteCommon) objectToCompare).getSections().size() != this.getSections().size()) {
            return false;
        }

        ListIterator iter1 = ((InfoSiteCommon) objectToCompare).getSections().listIterator();
        ListIterator iter2 = this.getSections().listIterator();
        while (result && iter1.hasNext()) {
            InfoSection infoSection1 = (InfoSection) iter1.next();
            InfoSection infoSection2 = (InfoSection) iter2.next();
            if (!infoSection1.equals(infoSection2)) {
                result = false;
                break;
            }
        }

        return result;
    }

    /**
     * @return
     */
    public List getAssociatedDegrees() {
        return associatedDegrees;
    }

    public List getDegrees() {
        List infoDegreeList = new ArrayList();
        if (associatedDegrees != null) {
            Iterator iter = associatedDegrees.iterator();
            while (iter.hasNext()) {
                InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
                InfoDegree infoDegree = infoCurricularCourse.getInfoDegreeCurricularPlan().getInfoDegree();
                if (!infoDegreeList.contains(infoDegree)) {
                    infoDegreeList.add(infoDegree);
                }
            }
            return infoDegreeList;
        }

        return null;

    }

    /**
     * @return
     */
    public String getMail() {
        return mail;
    }

    /**
     * @return
     */
    public List getSections() {
        return sections;
    }

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param list
     */
    public void setAssociatedDegrees(List list) {
        associatedDegrees = list;
    }

    /**
     * @param string
     */
    public void setMail(String string) {
        mail = string;
    }

    /**
     * @param list
     */
    public void setSections(List list) {
        sections = list;
    }

    /**
     * @param string
     */
    public void setTitle(String string) {
        title = string;
    }

    /**
     * @return
     */
    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * @param course
     */
    public void setExecutionCourse(InfoExecutionCourse course) {
        executionCourse = course;
    }

    /**
     * @return Returns the associatedDegreesByDegree.
     */
    public List getAssociatedDegreesByDegree() {
        return associatedDegreesByDegree;
    }

    /**
     * @param associatedDegreesByDegree
     *            The associatedDegreesByDegree to set.
     */
    public void setAssociatedDegreesByDegree(List associatedDegreesByDegree) {
        this.associatedDegreesByDegree = associatedDegreesByDegree;
    }
}