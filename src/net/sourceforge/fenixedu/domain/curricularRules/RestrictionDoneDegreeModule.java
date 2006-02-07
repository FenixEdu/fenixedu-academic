/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class RestrictionDoneDegreeModule extends RestrictionDoneDegreeModule_Base {
    
    /**
     * This constructor should be used in context of Composite Rule 
     */    
    protected RestrictionDoneDegreeModule(DegreeModule doneDegreeModule) {        
        super();        
        if (doneDegreeModule == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }        
        setDoneDegreeModule(doneDegreeModule);
    }

    public RestrictionDoneDegreeModule(DegreeModule degreeModuleToApplyRule, DegreeModule doneDegreeModule,
            CourseGroup contextCourseGroup, CurricularPeriodInfoDTO curricularPeriodInfoDTO, 
            ExecutionPeriod begin, ExecutionPeriod end, CurricularRuleType ruleType) {
        
        this(doneDegreeModule);
        
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
    public String getLabel() {
        // TODO Auto-generated method stub
        //return "curricularRule.restrictionDoneDegreeModule";
        final StringBuilder result = new StringBuilder();
        if (belongsToCompositeRule()) {
            result.append("Precedência do módulo não feito ");
        } else {
            result.append("Precedência do módulo feito ");
        }        
        result.append(getDoneDegreeModule().getName());
        result.append(" sobre ");
        result.append(getDegreeModuleToApplyRule().getName());
        if (getContextCourseGroup() != null) {
            result.append(" no contexto ");
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
