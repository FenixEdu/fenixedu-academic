package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CourseGroup extends CourseGroup_Base {

    protected CourseGroup() {
        super();
        setOjbConcreteClass(CourseGroup.class.getName());
    }
    
    public CourseGroup(String name, String code, String acronym) {
        this();
        setName(name);
        setCode(code);
        setAcronym(acronym);
    }
    
    public boolean isLeaf() {
        return false;   
    }
    
    public Boolean getCanBeDeleted() {
        return super.getCanBeDeleted() && !hasAnyCourseGroupContexts(); 
    }
    
    public void delete() {
        if(getCanBeDeleted()) {
            super.delete();
            super.deleteDomainObject();
        } else {
            throw new DomainException("error.notEmptyCourseGroupContexts");
        }
    }

    public void print(StringBuffer dcp, String tabs, IContext previousContext) {
        String tab = tabs + "\t";
        dcp.append(tab);
        dcp.append("[CG ").append(this.getIdInternal()).append("] ").append(this.getName()).append("\n");
        
        for (IContext context : this.getCourseGroupContexts()) {
            context.getDegreeModule().print(dcp, tab, context);
        }
    }
    
    public List<IContext> getContextsWithCurricularCourses() {
        List<IContext> result = new ArrayList<IContext>();
        for (IContext context : this.getCourseGroupContexts()) {
            if (context.getDegreeModule() instanceof ICurricularCourse) {
                result.add(context);
            }
        }
        
        return result;
    }

    public List<IContext> getContextsWithCourseGroups() {
        List<IContext> result = new ArrayList<IContext>();
        for (IContext context : this.getCourseGroupContexts()) {
            if (context.getDegreeModule() instanceof ICourseGroup) {
                result.add(context);
            }
        }
        
        return result;
    }

    public Double computeEctsCredits() {
        Double result = 0.0;
        
        for (IContext context : this.getCourseGroupContexts()) {
            if (context.getDegreeModule() != null && context.getDegreeModule().computeEctsCredits() != null) {
                result += context.getDegreeModule().computeEctsCredits();    
            }
        }
        
        return result;
    }
}
