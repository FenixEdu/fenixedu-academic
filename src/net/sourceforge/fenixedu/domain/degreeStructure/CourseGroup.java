package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CourseGroup extends CourseGroup_Base {

    protected CourseGroup() {
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
    
    public void delete() {
        if(!getCourseGroupContexts().isEmpty()) {
            throw new DomainException("can't delete Course Group");
        }
        super.delete();
        deleteDomainObject();
    }

    public void print(StringBuffer dcp, String tabs, IContext previousContext) {
        String tab = tabs + "\t";
        dcp.append(tab);
        dcp.append("[CG ").append(this.getIdInternal()).append("] ").append(this.getName()).append("\n");
        
        for (IContext context : this.getCourseGroupContexts()) {
            context.getDegreeModule().print(dcp, tab, context);
        }
    }

}
