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
public class InfoLessonWithInfoRoomAndInfoExecutionCourse extends
        InfoLessonWithInfoRoom {

    public static InfoLesson copyFromDomain(IAula lesson) {
        InfoLesson infoLesson = InfoLessonWithInfoRoom.copyFromDomain(lesson);
        if (infoLesson != null) {
            infoLesson
                    .setInfoDisciplinaExecucao(InfoExecutionCourseWithExecutionPeriod
                            .copyFromDomain(lesson.getDisciplinaExecucao()));
        }
        return infoLesson;
    }

}