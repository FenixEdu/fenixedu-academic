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
 * Created on Dec 12, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.professorship;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;

/**
 * @author jpvl
 */
public class DetailedProfessorship extends DataTranferObject {
    private InfoProfessorship infoProfessorship;

    private Boolean responsibleFor;

    private List executionCourseCurricularCoursesList;

    private Boolean masterDegreeOnly = Boolean.TRUE;

    /**
     * @return Returns the executionCourseCurricularCoursesList.
     */
    public List getExecutionCourseCurricularCoursesList() {
        return this.executionCourseCurricularCoursesList;
    }

    /**
     * @param executionCourseCurricularCoursesList
     *            The executionCourseCurricularCoursesList to set.
     */
    public void setExecutionCourseCurricularCoursesList(List executionCourseCurricularCoursesList) {
        this.executionCourseCurricularCoursesList = executionCourseCurricularCoursesList;
    }

    /**
     * @return Returns the infoProfessorship.
     */
    public InfoProfessorship getInfoProfessorship() {
        return this.infoProfessorship;
    }

    /**
     * @param infoProfessorship
     *            The infoProfessorship to set.
     */
    public void setInfoProfessorship(InfoProfessorship infoProfessorship) {
        this.infoProfessorship = infoProfessorship;
    }

    public List getInfoDegreeList() {

        List infoDegreeList = new ArrayList();
        Iterator iter = executionCourseCurricularCoursesList.iterator();
        while (iter.hasNext()) {
            InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
            InfoDegree infoDegree = infoCurricularCourse.getInfoDegreeCurricularPlan().getInfoDegree();
            if (!infoDegreeList.contains(infoDegree)) {
                infoDegreeList.add(infoDegree);
            }
        }
        return infoDegreeList;
    }

    /**
     * @return Returns the responsibleFor.
     */
    public Boolean getResponsibleFor() {
        return this.responsibleFor;
    }

    /**
     * @param responsibleFor
     *            The responsibleFor to set.
     */
    public void setResponsibleFor(Boolean responsibleFor) {
        this.responsibleFor = responsibleFor;
    }

    /**
     * @return Returns the masterDegreeOnly.
     */
    public Boolean getMasterDegreeOnly() {
        return masterDegreeOnly;
    }

    /**
     * @param masterDegreeOnly
     *            The masterDegreeOnly to set.
     */
    public void setMasterDegreeOnly(Boolean masterDegreeOnly) {
        this.masterDegreeOnly = masterDegreeOnly;
    }
}