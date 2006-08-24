package net.sourceforge.fenixedu.dataTransferObject.gesdis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreateLessonPlanningBean implements Serializable {

    private DomainReference<ExecutionCourse> executionCourseReference;
    
    private MultiLanguageString title;
    
    private MultiLanguageString planning;
    
    private ShiftType lessonType;

    public CreateLessonPlanningBean(ExecutionCourse executionCourse) {
        setExecutionCourse(executionCourse);
    }
    
    public ExecutionCourse getExecutionCourse() {
        return (this.executionCourseReference != null) ? this.executionCourseReference.getObject() : null;
    }
    
    public void setExecutionCourse(ExecutionCourse executionCourseReference) {
        this.executionCourseReference = (executionCourseReference != null) ? new DomainReference<ExecutionCourse>(executionCourseReference) : null;
    }

    public ShiftType getLessonType() {
        return lessonType;
    }

    public void setLessonType(ShiftType lessonType) {
        this.lessonType = lessonType;
    }

    public MultiLanguageString getPlanning() {
        return planning;
    }

    public void setPlanning(MultiLanguageString planning) {
        this.planning = planning;
    }

    public MultiLanguageString getTitle() {
        return title;
    }

    public void setTitle(MultiLanguageString title) {
        this.title = title;
    }     
}
