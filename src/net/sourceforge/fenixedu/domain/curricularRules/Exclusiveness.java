package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Exclusiveness extends Exclusiveness_Base {
    
    protected Exclusiveness(DegreeModule degreeModuleToApplyRule, DegreeModule exclusiveDegreeModule,
            CourseGroup contextCourseGroup, ExecutionPeriod begin, ExecutionPeriod end) {

        super();
        
        if (degreeModuleToApplyRule == null || begin == null || exclusiveDegreeModule == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        
        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setExclusiveDegreeModule(exclusiveDegreeModule);
        setCurricularRuleType(CurricularRuleType.EXCLUSIVENESS);
        
        setBegin(begin);
        setEnd(end);
        setContextCourseGroup(contextCourseGroup);
    }
    
    protected void edit(DegreeModule exclusiveDegreeModule, CourseGroup contextCourseGroup) {
        if (exclusiveDegreeModule == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (exclusiveDegreeModule != this.getExclusiveDegreeModule()) {
            removeRuleFromCurrentExclusiveDegreeModule();
            new Exclusiveness(exclusiveDegreeModule, getDegreeModuleToApplyRule(), contextCourseGroup, getBegin(), getEnd());
        }
        setExclusiveDegreeModule(exclusiveDegreeModule);
        setContextCourseGroup(contextCourseGroup);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        labelList.add(new GenericPair<Object, Boolean>("label.exclusiveness", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.between", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>(getDegreeModuleToApplyRule().getName(), false));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.and", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>(getExclusiveDegreeModule().getName(), false));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        
        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inGroup", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));            
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getName(), false));
        }
        return labelList;
    }

    @Override
    public boolean evaluate(Class< ? extends DomainObject> object) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void removeOwnParameters() {
        removeRuleFromCurrentExclusiveDegreeModule();
        removeExclusiveDegreeModule();
    }

    private void removeRuleFromCurrentExclusiveDegreeModule() {
        final Iterator<CurricularRule> curricularRulesIterator = this.getExclusiveDegreeModule().getCurricularRulesIterator();
        while (curricularRulesIterator.hasNext()) {
            final CurricularRule curricularRule = curricularRulesIterator.next();
            if (curricularRule.getCurricularRuleType() == this.getCurricularRuleType()) {
                final Exclusiveness exclusiveness = (Exclusiveness) curricularRule;
                if (exclusiveness.getExclusiveDegreeModule() == getDegreeModuleToApplyRule()) {
                    curricularRulesIterator.remove();
                    exclusiveness.removeExclusiveDegreeModule();
                    exclusiveness.removeCommonParameters();
                    exclusiveness.deleteDomainObject();
                }
            }
        }
    }
}
