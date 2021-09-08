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
package org.fenixedu.academic.domain.curricularRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.ExclusivenessVerifier;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.GenericPair;

public class Exclusiveness extends Exclusiveness_Base {

    protected Exclusiveness(final DegreeModule toApplyRule, final DegreeModule exclusiveDegreeModule,
            final CourseGroup contextCourseGroup, final ExecutionSemester begin, final ExecutionSemester end) {

        super();
        checkParameters(toApplyRule, exclusiveDegreeModule);
        init(toApplyRule, contextCourseGroup, begin, end, CurricularRuleType.EXCLUSIVENESS);
        setExclusiveDegreeModule(exclusiveDegreeModule);
    }

    private void checkParameters(final DegreeModule toApplyRule, final DegreeModule exclusiveDegreeModule) {
        if (exclusiveDegreeModule == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (toApplyRule == exclusiveDegreeModule) {
            throw new DomainException("curricular.rule.invalid.parameters.degreeModules.must.be.different");
        }
        if (toApplyRule.isLeaf()) {
            if (!exclusiveDegreeModule.isLeaf()) {
                throw new DomainException("curricular.rule.invalid.parameters.degreeModules.must.have.same.type");
            }
        } else if (exclusiveDegreeModule.isLeaf()) {
            throw new DomainException("curricular.rule.invalid.parameters.degreeModules.must.have.same.type");
        }
    }

    protected void edit(final DegreeModule exclusiveDegreeModule, final CourseGroup contextCourseGroup) {
        checkParameters(getDegreeModuleToApplyRule(), exclusiveDegreeModule);
        if (exclusiveDegreeModule != getExclusiveDegreeModule()) {
            removeRuleFromCurrentExclusiveDegreeModule(this.getExclusiveDegreeModule().getCurricularRulesSet().iterator());
            new Exclusiveness(exclusiveDegreeModule, getDegreeModuleToApplyRule(), contextCourseGroup, getBegin(), getEnd());
        }
        setExclusiveDegreeModule(exclusiveDegreeModule);
        setContextCourseGroup(contextCourseGroup);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return getLabel(null);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel(final ExecutionSemester executionSemester) {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        labelList.add(new GenericPair<Object, Boolean>("label.exclusiveness", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.between", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));

        // getting full name only for course groups
        String degreeModuleToApplyRule =
                (getDegreeModuleToApplyRule().isLeaf()) ? getDegreeModuleToApplyRule().getNameI18N(executionSemester).getContent() : getDegreeModuleToApplyRule()
                        .getOneFullName(executionSemester);
        String exclusiveDegreeModule =
                (getExclusiveDegreeModule().isLeaf()) ? getExclusiveDegreeModule().getNameI18N(executionSemester).getContent() : getExclusiveDegreeModule()
                        .getOneFullName(executionSemester);

        labelList.add(new GenericPair<Object, Boolean>(degreeModuleToApplyRule, false));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.and", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>(exclusiveDegreeModule, false));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));

        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inGroup", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(executionSemester), false));
        }
        return labelList;
    }

    @Override
    protected void removeOwnParameters() {
        removeRuleFromCurrentExclusiveDegreeModule(this.getExclusiveDegreeModule().getCurricularRulesSet().iterator());
        setExclusiveDegreeModule(null);
    }

    private void removeRuleFromCurrentExclusiveDegreeModule(final Iterator<CurricularRule> curricularRulesIterator) {
        while (curricularRulesIterator.hasNext()) {
            final CurricularRule curricularRule = curricularRulesIterator.next();
            if (curricularRule.getCurricularRuleType() == null) { // (composite
                // rule)
                final CompositeRule compositeRule = (CompositeRule) curricularRule;
                removeRuleFromCurrentExclusiveDegreeModule(compositeRule.getCurricularRulesSet().iterator());
            } else if (curricularRule.getCurricularRuleType() == this.getCurricularRuleType()) {
                removeExclusivenessRule(curricularRulesIterator, (Exclusiveness) curricularRule);
            }
        }
    }

    private void removeExclusivenessRule(final Iterator<CurricularRule> curricularRulesIterator, final Exclusiveness exclusiveness)
            throws DomainException {

        if (exclusiveness.getExclusiveDegreeModule() == getDegreeModuleToApplyRule()) {
            if (exclusiveness.belongsToCompositeRule()) {
                if (this.belongsToCompositeRule()) { // both belong to
                    // composite rules ?
                    new Exclusiveness(exclusiveness.getExclusiveDegreeModule(), exclusiveness.getDegreeModuleToApplyRule(),
                            exclusiveness.getContextCourseGroup(), exclusiveness.getBegin(), exclusiveness.getEnd());
                    return;
                } else {
                    throw new DomainException("error.cannot.delete.rule.because.belongs.to.composite.rule", exclusiveness
                            .getDegreeModuleToApplyRule().getName());
                }
            }
            curricularRulesIterator.remove();
            exclusiveness.setExclusiveDegreeModule(null);
            exclusiveness.removeCommonParameters();
            exclusiveness.deleteDomainObject();
        }
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return new ExclusivenessVerifier();
    }

}
