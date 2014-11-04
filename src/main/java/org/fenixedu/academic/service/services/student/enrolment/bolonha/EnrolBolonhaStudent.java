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
package net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment.bolonha;

import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import pt.ist.fenixframework.Atomic;

public class EnrolBolonhaStudent {

    @Atomic
    public static RuleResult run(final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
            final List<IDegreeModuleToEvaluate> degreeModulesToEnrol, final List<CurriculumModule> curriculumModulesToRemove,
            final CurricularRuleLevel curricularRuleLevel) {
        return studentCurricularPlan.enrol(executionSemester, new HashSet<IDegreeModuleToEvaluate>(degreeModulesToEnrol),
                curriculumModulesToRemove, curricularRuleLevel);
    }
}
