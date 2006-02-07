/*
 * Created on Feb 7, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeModulesSelectionLimit extends DegreeModulesSelectionLimit_Base {
    
    /**
     * This constructor should be used in context of Composite Rule 
     */ 
    protected DegreeModulesSelectionLimit(Integer minimum, Integer maximum) {
        super();
        if (minimum == null || maximum == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (minimum.intValue() > maximum.intValue()) {
            throw new DomainException("error.minimum.greater.than.maximum");
        }
        setMinimum(minimum);
        setMaximum(maximum);
    }
    
    public DegreeModulesSelectionLimit(DegreeModule degreeModuleToApplyRule,
            CourseGroup contextCourseGroup, ExecutionPeriod begin, ExecutionPeriod end,
            CurricularRuleType curricularRuleType, Integer minimum, Integer maximum) {
        
        this(minimum, maximum);
        
        if (degreeModuleToApplyRule == null || begin == null || curricularRuleType == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        
        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setContextCourseGroup(contextCourseGroup);
        setBegin(begin);
        setEnd(end);
        setCurricularRuleType(curricularRuleType);
    }
    
    @Override
    public ExecutionPeriod getBegin() {
        return (getParentCompositeRule() != null) ? getParentCompositeRule().getBegin() : super.getBegin();
    }
    
    @Override
    public ExecutionPeriod getEnd() {
        return (getParentCompositeRule() != null) ? getParentCompositeRule().getEnd() : super.getEnd();
    }

    @Override
    public CurricularRuleType getCurricularRuleType() {
        return (getParentCompositeRule() != null) ? getParentCompositeRule().getCurricularRuleType() : super.getCurricularRuleType();
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {        
        return (getParentCompositeRule() != null) ? getParentCompositeRule().getDegreeModuleToApplyRule() : super.getDegreeModuleToApplyRule();
    }
    
    @Override
    public CourseGroup getContextCourseGroup() {        
        return (getParentCompositeRule() != null) ? getParentCompositeRule().getContextCourseGroup() : super.getContextCourseGroup();
    }

    @Override
    public String getLabel() {
        // TODO Auto-generated method stub
        final StringBuilder result = new StringBuilder();
        if (belongsToCompositeRule()) {            
            result.append("Deverá realizar entre ????");
        } else {
            result.append("Deverá realizar ");
        }        
        result.append(getMinimum());
        result.append(" a ");
        result.append(getMaximum());
        result.append(" módulos");
        if (getContextCourseGroup() != null) {
            result.append(" apenas contexto ");
            result.append(getContextCourseGroup().getName());
        }
        return result.toString();
    }

    @Override
    public boolean evaluate(Class< ? extends DomainObject> object) {
        // TODO Auto-generated method stub
        return false;
    }

}
