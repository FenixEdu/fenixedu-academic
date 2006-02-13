package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
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
        return !hasAnyCourseGroupContexts();
    }

    public void delete() {
        if (getCanBeDeleted()) {
            super.delete();
            super.deleteDomainObject();
        } else {
            throw new DomainException("courseGroup.notEmptyCourseGroupContexts");
        }
    }

    public void print(StringBuilder dcp, String tabs, Context previousContext) {
        String tab = tabs + "\t";
        dcp.append(tab);
        dcp.append("[CG ").append(this.getIdInternal()).append("] ").append(this.getName()).append("\n");

        for (Context context : this.getCourseGroupContexts()) {
            context.getDegreeModule().print(dcp, tab, context);
        }
    }

    public List<Context> getContextsWithCurricularCourses() {
        List<Context> result = new ArrayList<Context>();
        for (Context context : this.getCourseGroupContexts()) {
            if (context.getDegreeModule() instanceof CurricularCourse) {
                result.add(context);
            }
        }

        return result;
    }

    public List<Context> getContextsWithCourseGroups() {
        List<Context> result = new ArrayList<Context>();
        for (Context context : this.getCourseGroupContexts()) {
            if (context.getDegreeModule() instanceof CourseGroup) {
                result.add(context);
            }
        }

        return result;
    }

    public Double getEctsCredits() {
        Double result = 0.0;

        for (Context context : this.getCourseGroupContexts()) {
            if (context.getDegreeModule() != null && context.getDegreeModule().getEctsCredits() != null) {
                result += context.getDegreeModule().getEctsCredits();
            }
        }

        return result;
    }

    protected void checkContextsFor(final CourseGroup parentCourseGroup,
            final CurricularPeriod curricularPeriod) {
        for (final Context context : this.getDegreeModuleContexts()) {
            if (context.getCourseGroup() == parentCourseGroup) {
                throw new DomainException("courseGroup.contextAlreadyExistForCourseGroup");
            }
        }
    }

    @Override
    @Checked("CourseGroupPredicates.curricularPlanMemberWritePredicate")
    public void setName(String name) {
        super.setName(name);
    }
    
    public void checkDuplicateChildNames(final String name, final String nameEn) throws DomainException  {
        String normalizedName = StringFormatter.normalize(name);
        String normalizedNameEn = StringFormatter.normalize(nameEn);
        if(!verifyNames(normalizedName, normalizedNameEn)){
            throw new DomainException("error.existingCourseGroupWithSameName");
        }
    }
    
    public void checkDuplicateBrotherNames(final String name, final String nameEn) throws DomainException  {
        String normalizedName = StringFormatter.normalize(name);
        String normalizedNameEn = StringFormatter.normalize(nameEn);
        for (Context parentContext : getDegreeModuleContexts()) {
            DegreeModule parentDegreeModule = parentContext.getDegreeModule();
            if (parentDegreeModule instanceof CourseGroup) {
                if(!((CourseGroup) parentDegreeModule).verifyNames(normalizedName, normalizedNameEn)){
                    throw new DomainException("error.existingCourseGroupWithSameName");
                }
            }           
        }
    }
    
    private boolean verifyNames(String normalizedName, String normalizedNameEn){
        for (Context context : getCourseGroupContexts()) {
            DegreeModule degreeModule = context.getDegreeModule();
            if (degreeModule != this) {                
                if(degreeModule.getName() != null && StringFormatter.normalize(degreeModule.getName()).equals(normalizedName)){
                    return false;
                }
                if(degreeModule.getNameEn() != null && StringFormatter.normalize(degreeModule.getNameEn()).equals(normalizedNameEn)){
                    return false;
                }
            }
        }
        return true;
    }
    
}
