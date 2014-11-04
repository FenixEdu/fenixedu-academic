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

import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;

public class CurriculumLineLocationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private CurriculumLine curriculumLine;

    private CurriculumGroup curriculumGroup;

    private boolean withRules = true;

    public CurriculumLineLocationBean() {

    }

    public CurriculumLineLocationBean(final CurriculumLine curriculumLine, final CurriculumGroup curriculumGroup,
            final boolean withRules) {
        setCurriculumLine(curriculumLine);
        setCurriculumGroup(curriculumGroup);
        withRules(withRules);
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

    public boolean isWithRules() {
        return withRules;
    }

    public void withRules(boolean value) {
        this.withRules = value;
    }
}
