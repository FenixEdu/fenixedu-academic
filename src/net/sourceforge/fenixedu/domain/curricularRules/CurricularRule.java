package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

public abstract class CurricularRule extends CurricularRule_Base {
 
    protected CurricularRule() {
        super();
        setOjbConcreteClass(getClass().getName());        
    }

    public void delete() {
        removeDegreeModuleToApplyRule();
        super.deleteDomainObject();
    }

    public boolean appliesToContext(Context context) {
        return this.appliesToCourseGroup(context);
    }
    
    private boolean appliesToCourseGroup(Context context) {
        return (this.getContextCourseGroup() == null || this.getContextCourseGroup().equals(context.getCourseGroup()));
    }
    
    public abstract List<GenericPair<Object, Boolean>> getLabel();

    public abstract boolean evaluate(Class<? extends DomainObject> object);
    
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
        return (getParentCompositeRule() != null) ? getParentCompositeRule().getCurricularRuleType()
                : super.getCurricularRuleType();
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
        return (getParentCompositeRule() != null) ? getParentCompositeRule()
                .getDegreeModuleToApplyRule() : super.getDegreeModuleToApplyRule();
    }

    @Override
    public CourseGroup getContextCourseGroup() {
        return (getParentCompositeRule() != null) ? getParentCompositeRule().getContextCourseGroup()
                : super.getContextCourseGroup();
    }
}
