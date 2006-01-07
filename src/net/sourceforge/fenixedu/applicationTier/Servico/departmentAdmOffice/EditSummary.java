/*
 * Created on 21/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.summary.SummaryUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Manuel Pinto e João Figueiredo
 */
public class EditSummary implements IService {

    public void run(Integer executionCourseId, InfoSummary infoSummary) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (infoSummary == null || infoSummary.getInfoShift() == null
                || infoSummary.getInfoShift().getIdInternal() == null
                || infoSummary.getIsExtraLesson() == null || infoSummary.getSummaryDate() == null) {
            throw new FenixServiceException("error.summary.impossible.edit");
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentSummary persistentSummary = persistentSupport.getIPersistentSummary();

        final Summary summary = (Summary) persistentSummary.readByOID(Summary.class, infoSummary
                .getIdInternal());
        
        final ExecutionCourse executionCourse = (ExecutionCourse) persistentSupport.getIPersistentObject().readByOID(
                ExecutionCourse.class, executionCourseId);
        
        final Shift shift = SummaryUtils.getShift(persistentSupport, summary, infoSummary);
        final Room room = SummaryUtils.getRoom(persistentSupport, summary, shift, infoSummary);

        shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                .getSummaryHour().getTime(), room, !summary.getShift().equals(shift));

        final Professorship professorship = SummaryUtils.getProfessorship(persistentSupport, infoSummary);
        if (professorship != null) {
            summary.edit(infoSummary.getTitle(), infoSummary.getSummaryText(), infoSummary
                    .getStudentsNumber(), infoSummary.getIsExtraLesson(), professorship);
            return;
        }

        final Teacher teacher = SummaryUtils.getTeacher(persistentSupport, infoSummary);
        if (teacher != null) {
            if (!executionCourse.teacherLecturesExecutionCourse(teacher)) {
                summary.edit(infoSummary.getTitle(), infoSummary.getSummaryText(), infoSummary
                        .getStudentsNumber(), infoSummary.getIsExtraLesson(), teacher);
                return;
            }
            else{
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