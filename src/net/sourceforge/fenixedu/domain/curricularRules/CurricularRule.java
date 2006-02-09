package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;

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

}
