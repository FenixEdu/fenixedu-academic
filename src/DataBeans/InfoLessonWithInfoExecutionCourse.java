/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.IAula;

/**
 * @author João Mota
 *  
 */
public class InfoLessonWithInfoExecutionCourse extends
        InfoLesson {

    public void copyFromDomain(IAula lesson) {
        super.copyFromDomain(lesson);
    }

    public static InfoLesson newInfoFromDomain(IAula lesson) {
        InfoLessonWithInfoExecutionCourse infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLessonWithInfoExecutionCourse();
            infoLesson.copyFromDomain(lesson);
        }
        return infoLesson;
    }

}