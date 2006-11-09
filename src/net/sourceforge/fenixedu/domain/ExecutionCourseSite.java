package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersAndStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class ExecutionCourseSite extends ExecutionCourseSite_Base {
    
    protected ExecutionCourseSite() {
        super();
        
        setRootDomainObject(RootDomainObject.getInstance());
        
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
    
}
