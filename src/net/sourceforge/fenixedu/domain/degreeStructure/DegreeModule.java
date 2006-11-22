package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LanguageUtils;


public abstract class DegreeModule extends DegreeModule_Base {

    public DegreeModule() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    /**
     * We need a method to return a full name of a course group - from it's parent course group to
     * the degree curricular plan's root course group.
     * 
     * Given this is impossible, for there are many routes from the root course group to one
     * particular course group, we choose (for now...) to get one possible full name, 
     * always visiting the first element of every list of contexts on our way to the root course group.  
     * 
     * @return A string with one possible full name of this course group
     */
    public String getOneFullName() {
	boolean pt = !(LanguageUtils.getUserLanguage() == Language.en);
	
        StringBuilder result = new StringBuilder();
        
        DegreeModule iter = this; 
        result.append((pt) ? iter.getName() : iter.getNameEn());
        if (iter.isRoot()) {
            return result.toString();
        } else {
            iter = iter.getParentContexts().get(0).getParentCourseGroup();
            
            for (;iter.hasAnyParentContexts(); iter = iter.getParentContexts().get(0).getParentCourseGroup()) {
                result.insert(0, ((pt) ? iter.getName() : iter.getNameEn()) + " > ");
            }
            
            return result.toString();
        }
    }
    
    public void delete() {
        for (;!getParentContexts().isEmpty(); getParentContexts().get(0).delete());
        for (;!getCurricularRules().isEmpty(); getCurricularRules().get(0).delete());
        for (;!getParticipatingPrecedenceCurricularRules().isEmpty(); getParticipatingPrecedenceCurricularRules().get(0).delete());
        for (;!getParticipatingExclusivenessCurricularRules().isEmpty(); getParticipatingExclusivenessCurricularRules().get(0).delete());
    }
    
    public Context addContext(CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
            ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
        
        if (this.isRoot()) {
            throw new DomainException("degreeModule.cannot.add.context.to.root");
        }
        
        checkContextsFor(parentCourseGroup, curricularPeriod, null);
        checkOwnRestrictions(parentCourseGroup, curricularPeriod);
        return new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }
    
    public void editContext(Context context, CourseGroup parentCourseGroup,
            CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod,
            ExecutionPeriod endExecutionPeriod) {
        
        checkContextsFor(parentCourseGroup, curricularPeriod, context);
        checkOwnRestrictions(parentCourseGroup, curricularPeriod);
        context.edit(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod,
                endExecutionPeriod);
    }
    
    public void deleteContext(Context context) {        
        if (hasParentContexts(context)) {
            context.delete();
        }
        if (!hasAnyParentContexts()) {
            delete();
        }
    }
    
    public Set<CourseGroup> getAllParentCourseGroups() {
        Set<CourseGroup> result = new HashSet<CourseGroup>();
        collectParentCourseGroups(result, this);
        return result;
    }

    private void collectParentCourseGroups(Set<CourseGroup> result, DegreeModule module) {
        for (Context parent : module.getParentContexts()) {
            if (!parent.getParentCourseGroup().isRoot()) {
                result.add(parent.getParentCourseGroup());
                collectParentCourseGroups(result, parent.getParentCourseGroup());
            }
        }
    }
    
    public List<CurricularRule> getParticipatingCurricularRules() {
        List<CurricularRule> result = new ArrayList<CurricularRule>();
        result.addAll(getParticipatingPrecedenceCurricularRules());
        result.addAll(getParticipatingExclusivenessCurricularRules());
        addOwnPartipatingCurricularRules(result);
        return result;
    }

    public List<CurricularRule> getCurricularRules(ExecutionYear executionYear) {
        List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (CurricularRule curricularRule : this.getCurricularRules()) {
            if (executionYear == null || curricularRule.isValid(executionYear)) {
                result.add(curricularRule);
            }
        }
        
        return result;
    }
    
    public List<CurricularRule> getCurricularRules(ExecutionPeriod executionPeriod) {
        List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (CurricularRule curricularRule : this.getCurricularRules()) {
            if (executionPeriod == null || curricularRule.isValid(executionPeriod)) {
                result.add(curricularRule);
            }
        }
        
        return result;
    }

    
    public abstract Double getEctsCredits();
    public abstract void print(StringBuilder stringBuffer, String tabs, Context previousContext);
    public abstract Boolean getCanBeDeleted();
    public abstract boolean isLeaf();
    public abstract boolean isRoot();
    public abstract DegreeCurricularPlan getParentDegreeCurricularPlan();
    protected abstract void checkContextsFor(final CourseGroup parentCourseGroup,
            final CurricularPeriod curricularPeriod, final Context context);
    protected abstract void addOwnPartipatingCurricularRules(final List<CurricularRule> result);
    protected abstract void checkOwnRestrictions(final CourseGroup parentCourseGroup,
            final CurricularPeriod curricularPeriod);

    public boolean isOptional() {
	return false;
    }

    public Degree getDegree() {
	return getParentDegreeCurricularPlan().getDegree();
    }
    
}
