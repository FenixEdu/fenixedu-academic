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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.IStudentCurricularPlanBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class StudentEnrolmentBean implements Serializable, IStudentCurricularPlanBean {

    private StudentCurricularPlan studentCurricularPlan;
    private ExecutionSemester executionSemester;
    private List<CurriculumModule> curriculumModules;
    private List<DegreeModuleToEnrol> degreeModulesToEnrol;
    private CurriculumModuleBean curriculumModuleBean;

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public List<CurriculumModule> getCurriculumModules() {
        if (this.curriculumModules == null) {
            return new ArrayList<CurriculumModule>();
        }

        List<CurriculumModule> result = new ArrayList<CurriculumModule>();
        for (CurriculumModule curriculumModule : this.curriculumModules) {
            result.add(curriculumModule);
        }

        return result;
    }

    public void setCurriculumModules(List<CurriculumModule> curriculumModules) {
        if (curriculumModules == null) {
            this.curriculumModules = null;
        } else {
            this.curriculumModules = new ArrayList<CurriculumModule>();
            for (CurriculumModule curriculumModule : curriculumModules) {
                this.curriculumModules.add(curriculumModule);
            }
        }
    }

    public List<DegreeModuleToEnrol> getDegreeModulesToEnrol() {
        return degreeModulesToEnrol;
    }

    public void setDegreeModulesToEnrol(List<DegreeModuleToEnrol> degreeModulesToEnrol) {
        this.degreeModulesToEnrol = degreeModulesToEnrol;
    }

    public CurriculumModuleBean getCurriculumModuleBean() {
        return curriculumModuleBean;
    }

    public void setCurriculumModuleBean(CurriculumModuleBean curriculumModuleBean) {
        this.curriculumModuleBean = curriculumModuleBean;
    }

    public Set<CurriculumModule> getInitialCurriculumModules() {
        return getInitialCurriculumModules(getCurriculumModuleBean());
    }

    private Set<CurriculumModule> getInitialCurriculumModules(CurriculumModuleBean curriculumModuleBean) {
        Set<CurriculumModule> result = new HashSet<CurriculumModule>();
        if (curriculumModuleBean.getCurricularCoursesEnroled().isEmpty() && curriculumModuleBean.getGroupsEnroled().isEmpty()) {
            result.add(curriculumModuleBean.getCurriculumModule());
        }

        for (CurriculumModuleBean moduleBean : curriculumModuleBean.getCurricularCoursesEnroled()) {
            result.add(moduleBean.getCurriculumModule());
        }

        for (CurriculumModuleBean moduleBean : curriculumModuleBean.getGroupsEnroled()) {
            result.addAll(getInitialCurriculumModules(moduleBean));
        }

        return result;
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }

}
