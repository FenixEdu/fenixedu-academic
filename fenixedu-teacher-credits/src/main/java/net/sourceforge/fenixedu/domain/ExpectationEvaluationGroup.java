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
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.exceptions.DomainException;

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
        List<ExpectationEvaluationGroup> groups =
                ExpectationEvaluationGroup.getEvaluatedExpectationEvaluationGroups(appraiser, executionYear);
        for (ExpectationEvaluationGroup group : groups) {
            if (group.getEvaluated().equals(evaluated)) {
                throw new DomainException("error.ExpectationEvaluationGroup.evaluatedTeacher.alreadyExists");
            }
        }
    }

    private void checkTeachersDepartments(Teacher appraiser, Teacher evaluated, ExecutionYear executionYear) {
        Department appraiserDepartment = appraiser.getLastDepartment(executionYear.getAcademicInterval());
        Department evaluatedDepartment = evaluated.getLastDepartment(executionYear.getAcademicInterval());
        if (appraiserDepartment == null || evaluatedDepartment == null || !appraiserDepartment.equals(evaluatedDepartment)) {
            throw new DomainException("error.ExpectationEvaluationGroup.invalid.departments");
        }
    }

    public static List<ExpectationEvaluationGroup> getEvaluatedExpectationEvaluationGroups(Teacher teacher,
            ExecutionYear executionYear) {
        List<ExpectationEvaluationGroup> result = new ArrayList<ExpectationEvaluationGroup>();
        for (ExpectationEvaluationGroup expectationEvaluationGroup : teacher.getEvaluatedExpectationEvaluationGroupsSet()) {
            if (expectationEvaluationGroup.getExecutionYear().equals(executionYear)) {
                result.add(expectationEvaluationGroup);
            }
        }
        return result;
    }

    public static boolean hasExpectationEvaluatedTeacher(Teacher teacher, Teacher target, ExecutionYear executionYear) {
        for (ExpectationEvaluationGroup group : teacher.getEvaluatedExpectationEvaluationGroupsSet()) {
            if (group.getExecutionYear().equals(executionYear) && group.getEvaluated().equals(target)) {
                return true;
            }
        }
        return false;
    }

}
