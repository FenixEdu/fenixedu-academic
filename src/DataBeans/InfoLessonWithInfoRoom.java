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
public class InfoLessonWithInfoRoom extends InfoLesson {

    public void copyFromDomain(ILesson lesson) {
        super.copyFromDomain(lesson);
        if (lesson != null) {
            setInfoSala(InfoRoom.newInfoFromDomain(lesson.getSala()));
        }
    }

    public static InfoLesson newInfoFromDomain(ILesson lesson) {
        InfoLessonWithInfoRoom infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLessonWithInfoRoom();
            infoLesson.copyFromDomain(lesson);
        }
        return infoLesson;
    }

}