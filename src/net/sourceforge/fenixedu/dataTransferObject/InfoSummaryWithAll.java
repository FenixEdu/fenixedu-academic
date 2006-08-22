/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Summary;

/**
 * @author João Mota
 *  
 */
public class InfoSummaryWithAll extends InfoSummary {

    public void copyFromDomain(Summary summary) {
        super.copyFromDomain(summary);
        if (summary != null) {
            setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod.newInfoFromDomain(summary
                    .getExecutionCourse()));
            setInfoShift(InfoShiftWithInfoExecutionCourseAndCollections.newInfoFromDomain(summary
                    .getShift()));
            setInfoProfessorship(InfoProfessorship.newInfoFromDomain(summary.getProfessorship()));
            setInfoTeacher(InfoTeacherWithPerson.newInfoFromDomain(summary.getTeacher()));
            setInfoRoom(InfoRoom.newInfoFromDomain(summary.getRoom()));
        }
    }

    public static InfoSummary newInfoFromDomain(Summary summary) {
        InfoSummaryWithAll infoSummary = null;
        if (summary != null) {
            infoSummary = new InfoSummaryWithAll();
            infoSummary.copyFromDomain(summary);
        }
        return infoSummary;
    }
}