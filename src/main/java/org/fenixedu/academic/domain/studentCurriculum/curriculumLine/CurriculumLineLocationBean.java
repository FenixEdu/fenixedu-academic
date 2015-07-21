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
package org.fenixedu.academic.domain.studentCurriculum.curriculumLine;

import java.io.Serializable;

import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;

@SuppressWarnings("serial")
public class CurriculumLineLocationBean implements Serializable {

    private CurriculumLine curriculumLine;

    private CurriculumGroup curriculumGroup;

    private boolean withContextInPlan = true;

    private boolean withRules = true;

    public CurriculumLineLocationBean() {
    }

    public CurriculumLineLocationBean(final CurriculumLine curriculumLine, final CurriculumGroup curriculumGroup,
            final boolean withRules) {
        setCurriculumLine(curriculumLine);
        setCurriculumGroup(curriculumGroup);
        setWithRules(withRules);
    }

    public CurriculumLine getCurriculumLine() {
        return this.curriculumLine;
    }

    public void setCurriculumLine(CurriculumLine curriculumLine) {
        this.curriculumLine = curriculumLine;
    }

    public CurriculumGroup getCurriculumGroup() {
        return this.curriculumGroup;
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
        this.curriculumGroup = curriculumGroup;
    }

    public static CurriculumLineLocationBean buildFrom(final CurriculumLine curriculumLine, final boolean withRules) {
        return new CurriculumLineLocationBean(curriculumLine, curriculumLine.getCurriculumGroup(), withRules);
    }

    public Student getStudent() {
        return getCurriculumLine().getStudent();
    }

    public boolean isWithContextInPlan() {
        return this.withContextInPlan;
    }

    public void setWithContextInPlan(final boolean input) {
        this.withContextInPlan = input;
    }

    public boolean isWithRules() {
        return withRules;
    }

    public void setWithRules(boolean value) {
        this.withRules = value;
    }

}
