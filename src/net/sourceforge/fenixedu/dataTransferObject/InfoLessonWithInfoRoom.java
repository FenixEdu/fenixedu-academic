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
public class InfoLessonWithInfoRoom extends InfoLesson {

    public void copyFromDomain(Lesson lesson) {
        super.copyFromDomain(lesson);
        if (lesson != null) {
            setInfoSala(InfoRoom.newInfoFromDomain(lesson.getSala()));
        }
    }

    public static InfoLesson newInfoFromDomain(Lesson lesson) {
        InfoLessonWithInfoRoom infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLessonWithInfoRoom();
            infoLesson.copyFromDomain(lesson);
        }
        return infoLesson;
    }

}