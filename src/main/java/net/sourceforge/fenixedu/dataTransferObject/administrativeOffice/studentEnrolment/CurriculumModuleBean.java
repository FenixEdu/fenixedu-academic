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
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CurriculumModuleBean implements Serializable {

    private List<CurriculumModuleBean> groupsEnroled;
    private List<CurriculumModuleBean> curricularCoursesEnroled;

    private List<DegreeModuleToEnrol> groupsToEnrol;
    private List<DegreeModuleToEnrol> curricularCoursesToEnrol;

    private CurriculumModule curriculumModule;

    public List<CurriculumModuleBean> getCurricularCoursesEnroled() {
        return curricularCoursesEnroled;
    }

    public void setCurricularCoursesEnroled(List<CurriculumModuleBean> curricularCoursesEnroled) {
        this.curricularCoursesEnroled = curricularCoursesEnroled;
    }

    public List<DegreeModuleToEnrol> getCurricularCoursesToEnrol() {
        return curricularCoursesToEnrol;
    }

    public void setCurricularCoursesToEnrol(List<DegreeModuleToEnrol> curricularCoursesToEnrol) {
        this.curricularCoursesToEnrol = curricularCoursesToEnrol;
    }

    public List<CurriculumModuleBean> getGroupsEnroled() {
        return groupsEnroled;
    }

    public void setGroupsEnroled(List<CurriculumModuleBean> groupsEnroled) {
        this.groupsEnroled = groupsEnroled;
    }

    public List<DegreeModuleToEnrol> getGroupsToEnrol() {
        return groupsToEnrol;
    }

    public void setGroupsToEnrol(List<DegreeModuleToEnrol> groupsToEnrol) {
        this.groupsToEnrol = groupsToEnrol;
    }

    public CurriculumModule getCurriculumModule() {
        return this.curriculumModule;
    }

    public void setCurriculumModule(CurriculumModule curriculumModule) {
        this.curriculumModule = curriculumModule;
    }

}
