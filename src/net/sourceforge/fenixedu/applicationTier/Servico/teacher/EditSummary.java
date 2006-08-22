/*
 * Created on 21/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.summary.SummaryUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Mota
 * @author Susana Fernandes 21/Jul/2003 fenix-head
 *         ServidorAplicacao.Servico.teacher
 * 
 * @author jdnf and mrsp 21/Jul/2005
 * 
 */
public class EditSummary extends Service {

    public void run(Integer executionCourseId, InfoSummary infoSummary) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (infoSummary == null || infoSummary.getInfoShift() == null
                || infoSummary.getInfoShift().getIdInternal() == null
                || infoSummary.getIsExtraLesson() == null || infoSummary.getSummaryDate() == null) {
            throw new FenixServiceException("error.summary.impossible.edit");
        }

        final Summary summary = rootDomainObject.readSummaryByOID(infoSummary
                .getIdInternal());

        final ExecutionCourse executionCourse = summary.getExecutionCourse();

        final Shift shift = SummaryUtils.getShift(summary, infoSummary);
        final OldRoom room = SummaryUtils.getRoom(summary, shift, infoSummary);

        shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                .getSummaryHour().getTime(), room, !summary.getShift().equals(shift));

        final Professorship professorship = SummaryUtils.getProfessorship(infoSummary);
        if (professorship != null) {
            summary.edit(infoSummary.getTitle(), infoSummary.getSummaryText(), infoSummary
                    .getStudentsNumber(), infoSummary.getIsExtraLesson(), professorship);
            return;
        }

        final Teacher teacher = SummaryUtils.getTeacher(infoSummary);
        if (teacher != null) {
            if (!executionCourse.teacherLecturesExecutionCourse(teacher)) {
                summary.edit(infoSummary.getTitle(), infoSummary.getSummaryText(), infoSummary
                        .getStudentsNumber(), infoSummary.getIsExtraLesson(), teacher);
                return;
            } else {
                throw new FenixServiceException("error.summary.teacher.invalid");
            }
        }

        String teacherName = infoSummary.getTeacherName();
        if (teacherName != null) {
            summary.edit(infoSummary.getTitle(), infoSummary.getSummaryText(), infoSummary
                    .getStudentsNumber(), infoSummary.getIsExtraLesson(), teacherName);
            return;
        }

        throw new FenixServiceException("error.summary.no.teacher");
    }
}