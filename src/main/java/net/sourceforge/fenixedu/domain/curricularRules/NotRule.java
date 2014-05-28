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

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class NotRule extends NotRule_Base {

    static {
        getRelationCurricularRuleNotRule().addListener(new RelationAdapter<CurricularRule, NotRule>() {
            @Override
            public void beforeAdd(CurricularRule curricularRule, NotRule notRule) {
                if (curricularRule.getParentCompositeRule() != null) {
                    throw new DomainException("error.curricular.rule.invalid.state");
                }
            }
        });
    }

    public NotRule(CurricularRule rule) {
        if (rule == null || rule.getParentCompositeRule() != null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }

        setDegreeModuleToApplyRule(rule.getDegreeModuleToApplyRule());
        rule.setDegreeModuleToApplyRule(null);
        setBegin(rule.getBegin());
        setEnd(rule.getEnd());
        setWrappedRule(rule);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return getWrappedRule().getLabel();
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    protected void removeOwnParameters() {
        getWrappedRule().delete();
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

    @Deprecated
    public boolean hasWrappedRule() {
        return getWrappedRule() != null;
    }

}
