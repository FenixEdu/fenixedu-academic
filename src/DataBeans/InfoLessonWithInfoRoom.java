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

    public void copyFromDomain(IAula lesson) {
        super.copyFromDomain(lesson);
        if (lesson != null) {
            setInfoSala(InfoRoom.newInfoFromDomain(lesson.getSala()));
        }
    }

    public static InfoLesson newInfoFromDomain(IAula lesson) {
        InfoLessonWithInfoRoom infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLessonWithInfoRoom();
            infoLesson.copyFromDomain(lesson);
        }
        return infoLesson;
    }

}