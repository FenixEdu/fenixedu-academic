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
public class InfoLessonWithInfoRoom extends InfoLesson {

    public static InfoLesson copyFromDomain(IAula lesson) {
        InfoLesson infoLesson = InfoLesson.copyFromDomain(lesson);
        if (infoLesson != null) {
            infoLesson.setInfoSala(InfoRoom.copyFromDomain(lesson.getSala()));
        }
        return infoLesson;
    }
    
}
