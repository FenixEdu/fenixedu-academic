package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersAndStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class ExecutionCourseSite extends ExecutionCourseSite_Base {
    
    protected ExecutionCourseSite() {
        super();
        
        setDynamicMailDistribution(false);
        setLessonPlanningAvailable(false);
    }

    public ExecutionCourseSite(ExecutionCourse course) {
        this();
        
        setExecutionCourse(course);
    }

    public void edit(final String initialStatement, final String introduction, final String mail,
            final String alternativeSite) {

        setInitialStatement(initialStatement);
        setIntroduction(introduction);
        setMail(mail);
        setAlternativeSite(alternativeSite);
    }
    
    public void copyCustomizationOptionsFrom(ExecutionCourseSite siteFrom) {
        setMail(siteFrom.getMail());
        setAlternativeSite(siteFrom.getAlternativeSite());
        setInitialStatement(siteFrom.getInitialStatement());
        setIntroduction(siteFrom.getIntroduction());
    }

    @Override
    protected void deleteRelations() {
        super.deleteRelations();
        
        removeRootDomainObject();
        removeExecutionCourse();
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
        List<IGroup> groups = super.getContextualPermissionGroups();
        
        ExecutionCourse executionCourse = getExecutionCourse();
        
        groups.add(new ExecutionCourseTeachersGroup(executionCourse));
        groups.add(new ExecutionCourseTeachersAndStudentsGroup(executionCourse));
        
        return groups;
    }

    @Override
    public IGroup getOwner() {
        return new ExecutionCourseTeachersGroup(getExecutionCourse());
    
    }

    @Override
    public String getAuthorName() {
        return getExecutionCourse().getNome();
    }
 
    @Override
    public ExecutionPeriod getExecutionPeriod() {
        return getExecutionCourse().getExecutionPeriod();
    }

    public static ExecutionCourseSite readExecutionCourseSiteByOID(Integer oid) {
        Site site = (Site) RootDomainObject.readDomainObjectByOID(ExecutionCourseSite.class, oid);
        if (site == null) {
            return null;
        }
        
        if (site instanceof ExecutionCourseSite) {
            return (ExecutionCourseSite) site;
        }
        else {
            return null;
        }
    }
}
