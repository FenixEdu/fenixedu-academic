/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto.administrativeOffice.studentEnrolment;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public interface NoCourseGroupEnrolmentBean {

    public NoCourseGroupCurriculumGroupType getGroupType();

    public StudentCurricularPlan getStudentCurricularPlan();

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan);

    public ExecutionInterval getExecutionPeriod();

    public void setExecutionPeriod(ExecutionInterval executionInterval);

    public CurriculumGroup getCurriculumGroup();

    public void setCurriculumGroup(CurriculumGroup curriculumGroup);

    public Context getContex();

    public void setContext(Context context);

    public CurricularCourse getSelectedCurricularCourse();

    public DegreeType getDegreeType();

    public void setDegreeType(DegreeType degreeType);

    public Degree getDegree();

    public void setDegree(Degree degree);

    public DegreeCurricularPlan getDegreeCurricularPlan();

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan);

    public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup();

    public CurricularRuleLevel getCurricularRuleLevel();

    public void setCurricularRuleLevel(CurricularRuleLevel curricularRuleLevel);
}
