package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

public class CompositeRule extends CompositeRule_Base {
    
    /**
     * This constructor should be used inside Composite Rule
     */
    protected CompositeRule(DegreeModule degreeModuleToApplyRule, LogicOperators compositeRuleType, CurricularRule... curricularRules) {
        checkCompositeRuleType(compositeRuleType, curricularRules);
        init(degreeModuleToApplyRule, null, null, null, compositeRuleType, curricularRules);
    }

    public CompositeRule(DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin,
            ExecutionPeriod end, CurricularRuleType ruleType, LogicOperators compositeRuleType,
            CurricularRule... curricularRules) {

        super();

        if (degreeModuleToApplyRule == null || begin == null || ruleType == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }

        checkCompositeRuleType(compositeRuleType, curricularRules);
        init(degreeModuleToApplyRule, begin, end, ruleType, compositeRuleType, curricularRules);
    }

    private void checkCompositeRuleType(LogicOperators compositeRuleType, CurricularRule... curricularRules) throws DomainException {
        if (compositeRuleType.equals(LogicOperators.NOT) && curricularRules.length > 1) {
            throw new DomainException("incorrect.NOT.composite.rule.use");
        } else if ((compositeRuleType.equals(LogicOperators.AND) || compositeRuleType
                .equals(LogicOperators.OR))
                && curricularRules.length < 2) {
            throw new DomainException("incorrect.composite.rule.use");
        } else if (!compositeRuleType.equals(LogicOperators.AND)
                && !compositeRuleType.equals(LogicOperators.OR)
                && !compositeRuleType.equals(LogicOperators.NOT)) {
            throw new DomainException("unsupported.composite.rule");
        }
    }

    private void init(DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin,
            ExecutionPeriod end, CurricularRuleType ruleType, LogicOperators compositeRuleType,
            CurricularRule... curricularRules) {

        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setBegin(begin);
        setEnd(end);
        setCurricularRuleType(ruleType);
        setCompositeRuleType(compositeRuleType);

        for (final CurricularRule curricularRule : curricularRules) {
            curricularRule.setParentCompositeRule(this);
        }
    }

    @Override
    public void delete() {
        removeDegreeModuleToApplyRule();
        for (; !getCurricularRules().isEmpty(); getCurricularRules().get(0).delete());
        super.deleteDomainObject();
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {

        if (getCompositeRuleType().equals(LogicOperators.NOT)) {
            return getCurricularRules().get(0).getLabel();
        } else {

            String operator = "";
            if (getCompositeRuleType().equals(LogicOperators.AND)) {
                operator = "label.and";
            } else if (getCompositeRuleType().equals(LogicOperators.OR)) {
                operator = "label.or";
            } else {
                throw new DomainException("unsupported.composite.rule");
            }

            List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();
            final Iterator<CurricularRule> curricularRulesIterator = getCurricularRules().listIterator();
            while (curricularRulesIterator.hasNext()) {
                labelList.addAll(curricularRulesIterator.next().getLabel());
                if (curricularRulesIterator.hasNext()) {
                    labelList.add(new GenericPair<Object, Boolean>(" ", false));
                    labelList.add(new GenericPair<Object, Boolean>(operator, true));
                    labelList.add(new GenericPair<Object, Boolean>(" ", false));
                }
            }

            return labelList;
        }
    }

    @Override
    public boolean evaluate(Class<? extends DomainObject> object) {
        boolean result = true;
        if (getCompositeRuleType().equals(LogicOperators.NOT)) {
            result = !getCurricularRules().get(0).evaluate(object);
        } else if (getCompositeRuleType().equals(LogicOperators.AND)) {
            for (final CurricularRule curricularRule : getCurricularRules()) {
                result &= curricularRule.evaluate(object);
                if (!result) {
                    return false;
                }
            }
        } else if (getCompositeRuleType().equals(LogicOperators.OR)) {
            for (final CurricularRule curricularRule : getCurricularRules()) {
                result |= curricularRule.evaluate(object);
                if (result) {
                    return true;
                }
            }
        } else {
            throw new DomainException("unsupported.composite.rule");
        }
        return result;
    }

    @Override
    public boolean appliesToContext(Context context) {
        for (CurricularRule curricularRule : this.getCurricularRules()) {
            if (!curricularRule.appliesToContext(context)) {
               return false; 
            }
        }
        return true;
    }

}
