/*
 * Created on Jan 20, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

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
        setCurricularRuleType(CurricularRuleType.PRECEDENCY_APPROVED_DEGREE_MODULE);
    }

    public RestrictionDoneDegreeModule(CurricularCourse degreeModuleToApplyRule, DegreeModule doneDegreeModule,
            CourseGroup contextCourseGroup, CurricularPeriodInfoDTO curricularPeriodInfoDTO, 
            ExecutionPeriod begin, ExecutionPeriod end) {
        
        this(doneDegreeModule);
        
        if (degreeModuleToApplyRule == null || begin == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setBegin(begin);
        setEnd(end);
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
    public List<GenericPair<Object, Boolean>> getLabel() {
        List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();
        
        if (belongsToCompositeRule()
                && getParentCompositeRule().getCompositeRuleType().equals(LogicOperators.NOT)) {
            labelList.add(new GenericPair<Object, Boolean>("label.precedenceNotDone", true));
        } else {
            labelList.add(new GenericPair<Object, Boolean>("label.precedenceDone", true));
        }

        labelList.add(new GenericPair<Object, Boolean>(": ", false));
        labelList.add(new GenericPair<Object, Boolean>(getDoneDegreeModule().getName(), false));

        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inContext", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getName(), false));
        }

        if (!getCurricularPeriodOrder().equals(0)) {
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.and", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.in", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getCurricularPeriodType().name(), true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getCurricularPeriodOrder(), false));
        }

        return labelList;
    }

    @Override
    public boolean evaluate(Class< ? extends DomainObject> object) {
        // TODO Auto-generated method stub
        return false;
    }
}
