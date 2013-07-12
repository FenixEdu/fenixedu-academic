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
