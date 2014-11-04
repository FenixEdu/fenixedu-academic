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
package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.StandaloneCurriculumGroup;

public class MaximumNumberOfEctsInStandaloneCurriculumGroup extends CurricularRuleNotPersistent {

    static public double MAXIMUM_DEFAULT_VALUE = 20.25d;

    private StandaloneCurriculumGroup standaloneCurriculumGroup;
    private double maximumEcts;

    public MaximumNumberOfEctsInStandaloneCurriculumGroup(final StandaloneCurriculumGroup standaloneCurriculumGroup) {
        this(standaloneCurriculumGroup, MAXIMUM_DEFAULT_VALUE);
    }

    public MaximumNumberOfEctsInStandaloneCurriculumGroup(final StandaloneCurriculumGroup standaloneCurriculumGroup,
            final double maximumEcts) {
        this.standaloneCurriculumGroup = standaloneCurriculumGroup;
        this.maximumEcts = maximumEcts;
    }

    @Override
    public CourseGroup getContextCourseGroup() {
        return null;
    }

    @Override
    public CurricularRuleType getCurricularRuleType() {
        return CurricularRuleType.MAXIMUM_NUMBER_OF_ECTS_IN_STANDALONE_CURRICULUM_GROUP;
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
        return null;
    }

    public StandaloneCurriculumGroup getStandaloneCurriculumGroup() {
        return standaloneCurriculumGroup;
    }

    @Override
    public ExecutionSemester getBegin() {
        return ExecutionSemester.readActualExecutionSemester();
    }

    @Override
    public ExecutionSemester getEnd() {
        return null;
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return Collections.singletonList(new GenericPair<Object, Boolean>("label.MaximumNumberOfEctsInStandaloneCurriculumGroup",
                true));
    }

    @Override
    public CompositeRule getParentCompositeRule() {
        return null;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

    public double getMaximumEcts() {
        return maximumEcts;
    }

    public boolean allowEcts(final double ects) {
        return ects <= maximumEcts;
    }

    static public boolean allowEctsCheckingDefaultValue(final double ects) {
        return ects <= MAXIMUM_DEFAULT_VALUE;
    }

}
