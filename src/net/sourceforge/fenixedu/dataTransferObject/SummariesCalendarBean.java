package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionCourse;


public class SummariesCalendarBean extends ShowSummariesBean {
   
    private LessonCalendarViewType calendarViewType;
    
    public SummariesCalendarBean(ExecutionCourse executionCourse) {
	setExecutionCourse(executionCourse);
	setCalendarViewType(LessonCalendarViewType.ALL_LESSONS);
    }    
           
    public LessonCalendarViewType getCalendarViewType() {
        return calendarViewType;
    }

    public void setCalendarViewType(LessonCalendarViewType calendarViewType) {
        this.calendarViewType = calendarViewType;
    }
             
    public static enum LessonCalendarViewType {
	ALL_LESSONS, PAST_LESSON, PAST_LESSON_WITHOUT_SUMMARY;
        public String getName() {
            return name();
        }
    }
}
