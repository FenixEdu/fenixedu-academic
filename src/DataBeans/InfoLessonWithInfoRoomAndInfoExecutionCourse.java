/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.ILesson;

/**
 * @author João Mota
 *  
 */
public class InfoLessonWithInfoRoomAndInfoExecutionCourse extends InfoLessonWithInfoRoom {

    public void copyFromDomain(ILesson lesson) {
        super.copyFromDomain(lesson);
    }

    public static InfoLesson newInfoFromDomain(ILesson lesson) {
        InfoLessonWithInfoRoomAndInfoExecutionCourse infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLessonWithInfoRoomAndInfoExecutionCourse();
            infoLesson.copyFromDomain(lesson);
        }
        return infoLesson;
    }

}