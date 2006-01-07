/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Lesson;

/**
 * @author João Mota
 *  
 */
public class InfoLessonWithInfoRoomAndInfoExecutionCourse extends InfoLessonWithInfoRoom {

    public void copyFromDomain(Lesson lesson) {
        super.copyFromDomain(lesson);
    }

    public static InfoLesson newInfoFromDomain(Lesson lesson) {
        InfoLessonWithInfoRoomAndInfoExecutionCourse infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLessonWithInfoRoomAndInfoExecutionCourse();
            infoLesson.copyFromDomain(lesson);
        }
        return infoLesson;
    }

}