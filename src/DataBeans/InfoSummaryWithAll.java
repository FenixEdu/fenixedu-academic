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

    public void copyFromDomain(ISummary summary) {
        super.copyFromDomain(summary);
        if (summary != null) {
            setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(summary.getExecutionCourse()));
            setInfoShift(InfoShiftWithInfoExecutionCourseAndCollections
                    .newInfoFromDomain(summary.getShift()));
            setInfoProfessorship(InfoProfessorshipWithAll
                    .newInfoFromDomain(summary.getProfessorship()));
            setInfoTeacher(InfoTeacherWithPerson.newInfoFromDomain(summary
                    .getTeacher()));
            setInfoRoom(InfoRoom.newInfoFromDomain(summary.getRoom()));
        }
    }

    public static InfoSummary newInfoFromDomain(ISummary summary) {
        InfoSummaryWithAll infoSummary = null;
        if (summary != null) {
            infoSummary = new InfoSummaryWithAll();
            infoSummary.copyFromDomain(summary);
        }
        return infoSummary;
    }
}