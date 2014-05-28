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
package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public interface IDegreeModuleToEvaluate {

    public static final Comparator<IDegreeModuleToEvaluate> COMPARATOR_BY_EXECUTION_PERIOD =
            new Comparator<IDegreeModuleToEvaluate>() {

                @Override
                public int compare(IDegreeModuleToEvaluate o1, IDegreeModuleToEvaluate o2) {
                    return o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
                }

            };

    public static final Comparator<IDegreeModuleToEvaluate> COMPARATOR_BY_CONTEXT = new Comparator<IDegreeModuleToEvaluate>() {

        @Override
        public int compare(IDegreeModuleToEvaluate o1, IDegreeModuleToEvaluate o2) {
            return o1.getContext().compareTo(o2.getContext());
        }

    };

    public CurriculumGroup getCurriculumGroup();

    public Context getContext();

    public DegreeModule getDegreeModule();

    public boolean isFor(final DegreeModule degreeModule);

    public ExecutionSemester getExecutionPeriod();

    public boolean isLeaf();

    public boolean isOptional();

    public boolean isEnroled();

    public boolean isEnroling();

    public boolean isDissertation();

    public boolean canCollectRules();

    public String getName();

    public String getYearFullLabel();

    public boolean isOptionalCurricularCourse();

    public String getKey();

    public Double getEctsCredits();

    public Double getEctsCredits(final ExecutionSemester executionSemester);

    public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester);

    public List<CurricularRule> getCurricularRulesFromDegreeModule(final ExecutionSemester executionSemester);

    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(final ExecutionSemester executionSemester);

    public boolean isAnnualCurricularCourse(final ExecutionYear executionYear);

}
