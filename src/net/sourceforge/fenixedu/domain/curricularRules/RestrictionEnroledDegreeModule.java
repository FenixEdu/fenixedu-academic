/*
 * Created on Feb 7, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

public class RestrictionEnroledDegreeModule extends RestrictionEnroledDegreeModule_Base {

    /**
     * This constructor should be used in context of Composite Rule 
     */    
    protected RestrictionEnroledDegreeModule(DegreeModule enroledDegreeModule) {        
        super();        
        if (enroledDegreeModule == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }        
        setEnroledDegreeModule(enroledDegreeModule);
    }

    public RestrictionEnroledDegreeModule(DegreeModule degreeModuleToApplyRule, DegreeModule enroledDegreeModule,
            CourseGroup contextCourseGroup, CurricularPeriodInfoDTO curricularPeriodInfoDTO, 
            ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleType ruleType) {
        
        this(enroledDegreeModule);
        
        if (degreeModuleToApplyRule == null || begin == null || ruleType == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setBegin(begin);
        setEnd(end);
        setCurricularRuleType(ruleType);
        setContextCourseGroup(contextCourseGroup);
        setCurricularPeriodType(curricularPeriodInfoDTO.getPeriodType());
        setCurricularPeriodOrder(curricularPeriodInfoDTO.getOrder());
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
        if (belongsToCompositeRule() && getParentCompositeRule().getCompositeRuleType().equals(LogicOperators.NOT)) {
            result.append("Precedência do módulo não inscrito ");
        } else {
            result.append("Precedência do módulo inscrito ");
        }        
        result.append(getEnroledDegreeModule().getName());
        result.append(" sobre ");
        result.append(getDegreeModuleToApplyRule().getName());
        if (getContextCourseGroup() != null) {
            result.append(" apenas no contexto ");
            result.append(getContextCourseGroup().getName());
        }
        if (!getCurricularPeriodOrder().equals(0)) {
            result.append(" e no ");
            result.append(getCurricularPeriodOrder());
            result.append(" ");
            result.append(getCurricularPeriodType().name());
        }
        return result.toString();
    }

    @Override
    public boolean evaluate(Class< ? extends DomainObject> object) {
        // TODO Auto-generated method stub
        return false;
    }

}
