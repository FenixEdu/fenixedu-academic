package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.util.StringFormatter;

public class CourseGroup extends CourseGroup_Base {

    protected CourseGroup() {
        super();
        setOjbConcreteClass(CourseGroup.class.getName());
    }

    public CourseGroup(String name, String nameEn) {
        this();
        super.setName(StringFormatter.prettyPrint(name));
        super.setNameEn(StringFormatter.prettyPrint(nameEn));
    }

    public boolean isLeaf() {
        return false;
    }

    public void edit(String name, String nameEn) throws FenixDomainException {
        this.checkDuplicateBrotherNames(name, nameEn);
        setName(StringFormatter.prettyPrint(name));
        setNameEn(StringFormatter.prettyPrint(nameEn));
    }

    public Boolean getCanBeDeleted() {
        return !hasAnyChildContexts();
    }

    public void delete() {
        if (getCanBeDeleted()) {
            removeParentDegreeCurricularPlan();
            super.delete();
            for (;!getParticipatingContextCurricularRules().isEmpty();getParticipatingContextCurricularRules().get(0).delete());
            super.deleteDomainObject();
        } else {
            throw new DomainException("courseGroup.notEmptyCourseGroupContexts");
        }
    }

    public void print(StringBuilder dcp, String tabs, Context previousContext) {
        String tab = tabs + "\t";
        dcp.append(tab);
        dcp.append("[CG ").append(this.getIdInternal()).append("] ").append(this.getName()).append("\n");

        for (Context context : this.getSortedContextsWithCurricularCourses()) {
            context.getDegreeModule().print(dcp, tab, context);
        }
        for (Context context : this.getSortedContextsWithCourseGroups()) {
            context.getDegreeModule().print(dcp, tab, context);
        }
    }

    public boolean isRoot() {
        return (super.getParentDegreeCurricularPlan() != null);
    }
    
    @Override
    public DegreeCurricularPlan getParentDegreeCurricularPlan() {
        if (isRoot()) {
            return super.getParentDegreeCurricularPlan();
        }
        return getDegreeModuleContexts().get(0).getParentCourseGroup().getParentDegreeCurricularPlan();
    }
    
    public List<Context> getSortedContextsWithCurricularCourses() {
        List<Context> result = new ArrayList<Context>();
        for (Context context : this.getChildContexts()) {
            if (context.getDegreeModule() instanceof CurricularCourse) {
                result.add(context);
            }
        }
        
        Collections.sort(result);        
        return result;
    }
    
    public List<Context> getSortedContextsWithCourseGroups() {
        List<Context> result = new ArrayList<Context>();
        for (Context context : this.getChildContexts()) {
            if (context.getDegreeModule() instanceof CourseGroup) {
                result.add(context);
            }
        }

        Collections.sort(result);        
        return result;
    }

    public Double getEctsCredits() {
        Double result = 0.0;

        for (Context context : this.getChildContexts()) {
            if (context.getDegreeModule() != null && context.getDegreeModule().getEctsCredits() != null) {
                result += context.getDegreeModule().getEctsCredits();
            }
        }

        return result;
    }
    
    public Context addContext(CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
            ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
        checkContextsFor(parentCourseGroup, curricularPeriod);
        parentCourseGroup.checkDuplicateChildNames(getName(), getNameEn());
        return new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    protected void checkContextsFor(final CourseGroup parentCourseGroup,
            final CurricularPeriod curricularPeriod) {
        for (final Context context : this.getDegreeModuleContexts()) {
            if (context.getParentCourseGroup() == parentCourseGroup) {
                throw new DomainException("courseGroup.contextAlreadyExistForCourseGroup");
            }
        }
    }

    @Override
    @Checked("CourseGroupPredicates.curricularPlanMemberWritePredicate")
    public void setName(String name) {
        super.setName(name);
    }

    public void checkDuplicateChildNames(final String name, final String nameEn) throws DomainException {
        String normalizedName = StringFormatter.normalize(name);
        String normalizedNameEn = StringFormatter.normalize(nameEn);
        if (!verifyNames(normalizedName, normalizedNameEn)) {
            throw new DomainException("error.existingCourseGroupWithSameName");
        }
    }

    public void checkDuplicateBrotherNames(final String name, final String nameEn)
            throws DomainException {
        String normalizedName = StringFormatter.normalize(name);
        String normalizedNameEn = StringFormatter.normalize(nameEn);
        for (Context parentContext : getDegreeModuleContexts()) {
            CourseGroup parentCourseGroup = parentContext.getParentCourseGroup();
            if (!parentCourseGroup.verifyNames(normalizedName, normalizedNameEn, this)) {
                throw new DomainException("error.existingCourseGroupWithSameName");
            }
        }
    }

    private boolean verifyNames(String normalizedName, String normalizedNameEn) {
        return verifyNames(normalizedName, normalizedNameEn, this);
    }
    
    private boolean verifyNames(String normalizedName, String normalizedNameEn, DegreeModule excludedModule) {
        for (Context context : getChildContexts()) {
            DegreeModule degreeModule = context.getDegreeModule();
            if (degreeModule != excludedModule) {
                if (degreeModule.getName() != null
                        && StringFormatter.normalize(degreeModule.getName()).equals(normalizedName)) {
                    return false;
                }
                if (degreeModule.getNameEn() != null
                        && StringFormatter.normalize(degreeModule.getNameEn()).equals(normalizedNameEn)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void orderChild(Context contextToOrder, int position) {
        List<Context> newSort = null;
        if (contextToOrder.getDegreeModule() instanceof CurricularCourse) {
            newSort = this.getSortedContextsWithCurricularCourses(); 
        } else {
            newSort = this.getSortedContextsWithCourseGroups();
        }
        
        if (newSort.size() <= 1 || position < 0 || position > newSort.size()) {
            return;
        }
        
        newSort.remove(contextToOrder);
        newSort.add(position, contextToOrder);    
        
        for (int newOrder = 0; newOrder < newSort.size() ; newOrder++) {
            Context context = newSort.get(newOrder);
            
            if (context == contextToOrder && newOrder != position) {
                throw new DomainException("wrong.order.algorithm");
            }
            context.setOrder(newOrder);
        }
    }

    @Override
    protected void addOwnPartipatingCurricularRules(final List<CurricularRule> result) {
        result.addAll(getParticipatingContextCurricularRules());
    }

    public void collectChildDegreeModules(Class<? extends DegreeModule> clazz, final Set<DegreeModule> result) {
        for (final Context context : this.getChildContexts()) {
            if (context.getDegreeModule().getClass().equals(clazz)) {
                result.add(context.getDegreeModule());
            }
            if (context.getDegreeModule() instanceof CourseGroup) {
                ((CourseGroup) context.getDegreeModule()).collectChildDegreeModules(clazz, result);
            }
        }
    }
    
    public void collectChildDegreeModulesIncludingFullPath(Class< ? extends DegreeModule> clazz, List<List<DegreeModule>> result, 
            List<DegreeModule> previousDegreeModulesPath) {
        final List<DegreeModule> currentDegreeModulesPath = previousDegreeModulesPath;
        for (final Context context : this.getChildContexts()) {
            List<DegreeModule> newDegreeModulesPath = null;
            if (context.getDegreeModule().getClass().equals(clazz)) {
                newDegreeModulesPath = initNewDegreeModulesPath(newDegreeModulesPath, currentDegreeModulesPath, context.getDegreeModule());
                result.add(newDegreeModulesPath);
            }
            if (context.getDegreeModule() instanceof CourseGroup) {
                newDegreeModulesPath = initNewDegreeModulesPath(newDegreeModulesPath, currentDegreeModulesPath, context.getDegreeModule());
                ((CourseGroup) context.getDegreeModule()).collectChildDegreeModulesIncludingFullPath(clazz, result, newDegreeModulesPath);
            }
        }
    }

    private List<DegreeModule> initNewDegreeModulesPath(List<DegreeModule> newDegreeModulesPath,
            final List<DegreeModule> currentDegreeModulesPath, final DegreeModule degreeModule) {
        if (newDegreeModulesPath == null) {
            newDegreeModulesPath = new ArrayList<DegreeModule>(currentDegreeModulesPath);
            newDegreeModulesPath.add(degreeModule);
        }
        return newDegreeModulesPath;
    }

}
