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

    public void copyFromDomain(IAula lesson) {
        super.copyFromDomain(lesson);
        if (lesson != null) {
            setInfoDisciplinaExecucao(InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(lesson.getDisciplinaExecucao()));
        }
    }

    public static InfoLesson newInfoFromDomain(IAula lesson) {
        InfoLessonWithInfoRoomAndInfoExecutionCourse infoLesson = null;
        if (infoLesson != null) {
            infoLesson = new InfoLessonWithInfoRoomAndInfoExecutionCourse();
            infoLesson.copyFromDomain(lesson);
        }
        return infoLesson;
    }

}