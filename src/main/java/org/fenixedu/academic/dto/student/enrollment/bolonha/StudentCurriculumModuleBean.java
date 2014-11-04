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
package org.fenixedu.academic.dto.student.enrollment.bolonha;

import java.io.Serializable;

import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;

public abstract class StudentCurriculumModuleBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private CurriculumModule curriculumModule;

    public StudentCurriculumModuleBean(final CurriculumModule curriculumModule) {
        super();
        setCurriculumModule(curriculumModule);
    }

    public CurriculumModule getCurriculumModule() {
        return this.curriculumModule;
    }

    private void setCurriculumModule(CurriculumModule curriculumModule) {
        this.curriculumModule = curriculumModule;
    }

    protected StudentCurricularPlan getStudentCurricularPlan() {
        return curriculumModule.getStudentCurricularPlan();
    }

    protected Registration getRegistration() {
        return getStudentCurricularPlan().getRegistration();
    }
}
