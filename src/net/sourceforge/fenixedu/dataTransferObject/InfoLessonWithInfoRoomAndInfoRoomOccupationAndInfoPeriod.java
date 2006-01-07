/*
 * Created on 2005/05/11
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Lesson;

/**
 * 
 * @author Luis Cruz 
 */
public class InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod extends InfoLessonWithInfoRoom {

    @Override
    public void copyFromDomain(Lesson lesson) {
        super.copyFromDomain(lesson);
        if (lesson != null) {
            setInfoRoomOccupation(InfoRoomOccupationWithInfoRoomAndInfoPeriod.newInfoFromDomain(lesson.getRoomOccupation()));
        }
    }

    public static InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod newInfoFromDomain(Lesson lesson) {
        InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod();
            infoLesson.copyFromDomain(lesson);
        }
        return infoLesson;
    }

}