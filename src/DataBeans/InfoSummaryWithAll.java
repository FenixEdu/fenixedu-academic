/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.ISummary;

/**
 * @author João Mota
 *  
 */
public class InfoSummaryWithAll extends InfoSummary {

    public static InfoSummary copyFromDomain(ISummary summary) {
        InfoSummary infoSummary = InfoSummary.copyFromDomain(summary);
        if (infoSummary != null) {
            infoSummary
                    .setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod
                            .copyFromDomain(summary.getExecutionCourse()));
            infoSummary
                    .setInfoShift(InfoShiftWithInfoExecutionCourseAndCollections
                            .copyFromDomain(summary.getShift()));
            infoSummary.setInfoProfessorship(InfoProfessorshipWithAll
                    .copyFromDomain(summary.getProfessorship()));
            infoSummary.setInfoTeacher(InfoTeacherWithPerson
                    .copyFromDomain(summary.getTeacher()));
            infoSummary.setInfoRoom(InfoRoom.copyFromDomain(summary.getRoom()));
        }
        return infoSummary;
    }

}