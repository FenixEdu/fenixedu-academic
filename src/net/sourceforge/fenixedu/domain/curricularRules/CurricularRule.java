package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

public abstract class CurricularRule extends CurricularRule_Base {
 
    protected CurricularRule() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());        
    }
    
    public void delete() {
        removeOwnParameters();
        removeCommonParameters();
        super.deleteDomainObject();
    }
    
    protected void removeCommonParameters() {
        removeDegreeModuleToApplyRule();
        removeBegin();
        removeEnd();
        removeParentCompositeRule();
        removeContextCourseGroup();
    }

    protected abstract void removeOwnParameters();

    public boolean appliesToContext(Context context) {
        return this.appliesToCourseGroup(context);
    }
    
    private boolean appliesToCourseGroup(Context context) {
        return (this.getContextCourseGroup() == null || this.getContextCourseGroup() == context.getParentCourseGroup());
    }
    
    public boolean isCompositeRule() {
        return getCurricularRuleType() == null;
    }
    
    protected boolean belongsToCompositeRule() {        
        return (getParentCompositeRule() != null);
    }
    
    public abstract boolean isLeaf();    
    public abstract List<GenericPair<Object, Boolean>> getLabel();
    public abstract boolean evaluate(Class<? extends DomainObject> object);
    
    @Override
    public ExecutionPeriod getBegin() {
        return belongsToCompositeRule() ? getParentCompositeRule().getBegin() : super.getBegin();
    }

    @Override
    public ExecutionPeriod getEnd() {
        return belongsToCompositeRule() ? getParentCompositeRule().getEnd() : super.getEnd();
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
        return belongsToCompositeRule() ? getParentCompositeRule()
                .getDegreeModuleToApplyRule() : super.getDegreeModuleToApplyRule();
    }

    @Override
    public CourseGroup getContextCourseGroup() {
        return belongsToCompositeRule() ? getParentCompositeRule().getContextCourseGroup()
                : super.getContextCourseGroup();
    }
    
    public boolean isValid(ExecutionPeriod executionPeriod) {
    	return (getBegin().isBeforeOrEquals(executionPeriod) && (getEnd() == null || getEnd().isAfterOrEquals(executionPeriod)));
    }
}
