/*
 * Created on 21/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
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
 * @author Susana Fernandes modified by Tânia Pousão 5/Abr/2004 21/Jul/2003
 *         fenix-head ServidorAplicacao.Servico.teacher
 * 
 * @author jdnf and mrsp 21/Jul/2005
 * 
 */
public class InsertSummary extends Service {

    public Boolean run(Integer executionCourseID, InfoSummary infoSummary) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (infoSummary == null || infoSummary.getInfoShift() == null
                || infoSummary.getInfoShift().getIdInternal() == null
                || infoSummary.getIsExtraLesson() == null || infoSummary.getSummaryDate() == null) {
            throw new FenixServiceException("error.summary.impossible.insert");
        }

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
        if (executionCourse == null)
            throw new InvalidArgumentsServiceException();

        final Shift shift = SummaryUtils.getShift(null, infoSummary);
        final OldRoom room = SummaryUtils.getRoom(null, shift, infoSummary);

        final Professorship professorship = SummaryUtils.getProfessorship(infoSummary);
        if (professorship != null) {
            final Summary summary = executionCourse.createSummary(infoSummary.getTitle(), infoSummary
                    .getSummaryText(), infoSummary.getStudentsNumber(), infoSummary.getIsExtraLesson(),
                    professorship);
            shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                    .getSummaryHour().getTime(), room, true);
            return true;
        }

        final Teacher teacher = SummaryUtils.getTeacher(infoSummary);
        if (teacher != null) {
            if (!executionCourse.teacherLecturesExecutionCourse(teacher)) {
                final Summary summary = executionCourse.createSummary(infoSummary.getTitle(),
                        infoSummary.getSummaryText(), infoSummary.getStudentsNumber(), infoSummary
                                .getIsExtraLesson(), teacher);
                shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                        .getSummaryHour().getTime(), room, true);
                return true;
            } else {
                throw new FenixServiceException("error.summary.teacher.invalid");
            }
        }

        String teacherName = infoSummary.getTeacherName();
        if (teacherName != null) {
            final Summary summary = executionCourse.createSummary(infoSummary.getTitle(), infoSummary
                    .getSummaryText(), infoSummary.getStudentsNumber(), infoSummary.getIsExtraLesson(),
                    teacherName);
            shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                    .getSummaryHour().getTime(), room, true);
            return true;
        }

        throw new FenixServiceException("error.summary.no.teacher");
    }
}