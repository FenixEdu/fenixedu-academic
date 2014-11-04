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
package net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleMoveWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;

public class MoveCurriculumLinesBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private StudentCurricularPlan studentCurricularPlan;
    private List<CurriculumLineLocationBean> curriculumLineLocations;
    private boolean withRules = true;

    public MoveCurriculumLinesBean() {
        this.curriculumLineLocations = new ArrayList<CurriculumLineLocationBean>();
    }

    public MoveCurriculumLinesBean(final StudentCurricularPlan studentCurricularPlan) {
        this();
        setStudentCurricularPlan(studentCurricularPlan);
    }

    public List<CurriculumLineLocationBean> getCurriculumLineLocations() {
        return curriculumLineLocations;
    }

    public void setCurriculumLineLocations(List<CurriculumLineLocationBean> curriculumLineLocations) {
        this.curriculumLineLocations = curriculumLineLocations;
    }

    public void addCurriculumLineLocation(final CurriculumLineLocationBean curriculumLineLocationBean) {
        this.curriculumLineLocations.add(curriculumLineLocationBean);
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public Set<IDegreeModuleToEvaluate> getIDegreeModulesToEvaluate(final ExecutionSemester executionSemester) {
        final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>();
        for (final CurriculumLineLocationBean bean : this.curriculumLineLocations) {
            result.add(CurriculumModuleMoveWrapper.create(bean.getCurriculumGroup(), executionSemester));
        }
        return result;
    }

    static public MoveCurriculumLinesBean buildFrom(final List<CurriculumLine> curriculumLines, final boolean withRules) {
        final MoveCurriculumLinesBean result = new MoveCurriculumLinesBean();
        for (final CurriculumLine curriculumLine : curriculumLines) {
            result.addCurriculumLineLocation(CurriculumLineLocationBean.buildFrom(curriculumLine, withRules));
        }
        return result;
    }

    public boolean isWithRules() {
        return withRules;
    }

    public void withRules(boolean value) {
        this.withRules = value;
    }

}
