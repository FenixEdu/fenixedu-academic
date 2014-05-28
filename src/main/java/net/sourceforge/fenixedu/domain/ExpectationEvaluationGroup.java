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
package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class ExpectationEvaluationGroup extends ExpectationEvaluationGroup_Base {

    private ExpectationEvaluationGroup() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ExpectationEvaluationGroup(Teacher appraiser, Teacher evaluated, ExecutionYear executionYear) {
        this();

        if (appraiser != null && evaluated != null) {
            if (appraiser.equals(evaluated)) {
                throw new DomainException("error.ExpectationEvaluationGroup.equals.teachers");
            }
            checkIfEvaluatedTeacherAlreadyExists(appraiser, evaluated, executionYear);
            checkTeachersDepartments(appraiser, evaluated, executionYear);
        }

        setAppraiser(appraiser);
        setEvaluated(evaluated);
        setExecutionYear(executionYear);
    }

    @Override
    public void setAppraiser(Teacher appraiser) {
        if (appraiser == null) {
            throw new DomainException("error.ExpectationEvaluationGroup.empty.appraiser");
        }
        super.setAppraiser(appraiser);
    }

    @Override
    public void setEvaluated(Teacher evaluated) {
        if (evaluated == null) {
            throw new DomainException("error.ExpectationEvaluationGroup.empty.evaluated");
        }
        super.setEvaluated(evaluated);
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
        if (executionYear == null) {
            throw new DomainException("error.ExpectationEvaluationGroup.empty.executionYear");
        }
        super.setExecutionYear(executionYear);
    }

    public void delete() {
        super.setAppraiser(null);
        super.setEvaluated(null);
        super.setExecutionYear(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    private void checkIfEvaluatedTeacherAlreadyExists(Teacher appraiser, Teacher evaluated, ExecutionYear executionYear) {
        List<ExpectationEvaluationGroup> groups = appraiser.getEvaluatedExpectationEvaluationGroups(executionYear);
        for (ExpectationEvaluationGroup group : groups) {
            if (group.getEvaluated().equals(evaluated)) {
                throw new DomainException("error.ExpectationEvaluationGroup.evaluatedTeacher.alreadyExists");
            }
        }
    }

    private void checkTeachersDepartments(Teacher appraiser, Teacher evaluated, ExecutionYear executionYear) {
        Department appraiserDepartment =
                appraiser.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(),
                        executionYear.getEndDateYearMonthDay());
        Department evaluatedDepartment =
                evaluated.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(),
                        executionYear.getEndDateYearMonthDay());
        if (appraiserDepartment == null || evaluatedDepartment == null || !appraiserDepartment.equals(evaluatedDepartment)) {
            throw new DomainException("error.ExpectationEvaluationGroup.invalid.departments");
        }
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEvaluated() {
        return getEvaluated() != null;
    }

    @Deprecated
    public boolean hasAppraiser() {
        return getAppraiser() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
