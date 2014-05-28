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
 * Created on 24/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author João Mota
 * 
 *         24/Jul/2003 fenix-head DataBeans
 * 
 */
public class InfoSiteBasicCurricularCourses extends DataTranferObject implements ISiteComponent {

    private List basicCurricularCourses;

    private List nonBasicCurricularCourses;

    private InfoDegreeCurricularPlan infoDegreeCurricularPlan;

    /**
     * @return
     */
    public List getNonBasicCurricularCourses() {
        return nonBasicCurricularCourses;
    }

    public List getBasicCurricularCoursesIds() {
        Iterator iter = basicCurricularCourses.iterator();
        List result = new ArrayList();
        while (iter.hasNext()) {
            result.add(((InfoCurricularCourse) iter.next()).getExternalId());
        }
        return result;
    }

    /**
     * @param nonBasicCurricularCourses
     */
    public void setNonBasicCurricularCourses(List nonBasicCurricularCourses) {
        this.nonBasicCurricularCourses = nonBasicCurricularCourses;
    }

    /**
     * @return
     */
    public List getBasicCurricularCourses() {
        return basicCurricularCourses;
    }

    /**
     * @param curricularCourses
     */
    public void setBasicCurricularCourses(List curricularCourses) {
        this.basicCurricularCourses = curricularCourses;
        // TODO: uncomment the sort when the test data is fixed
        // Collections.sort(this.curricularCourses);
    }

    /**
     *  
     */
    public InfoSiteBasicCurricularCourses() {
    }

    /**
     * @return
     */
    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return infoDegreeCurricularPlan;
    }

    /**
     * @param plan
     */
    public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan plan) {
        infoDegreeCurricularPlan = plan;
    }

}