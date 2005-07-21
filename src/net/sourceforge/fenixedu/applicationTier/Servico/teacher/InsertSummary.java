/*
 * Created on 21/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.summary.SummaryUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * @author Susana Fernandes modified by Tânia Pousão 5/Abr/2004 21/Jul/2003
 *         fenix-head ServidorAplicacao.Servico.teacher
 *         
 * @author jdnf and mrsp 21/Jul/2005
 * 
 */
public class InsertSummary implements IService {

    public Boolean run(Integer executionCourseID, InfoSummary infoSummary) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (infoSummary == null || infoSummary.getInfoShift() == null
                || infoSummary.getInfoShift().getIdInternal() == null
                || infoSummary.getIsExtraLesson() == null || infoSummary.getSummaryDate() == null) {
            throw new FenixServiceException("error.summary.impossible.insert");
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);
        if (executionCourse == null)
            throw new InvalidArgumentsServiceException();

        final IShift shift = SummaryUtils.getShift(persistentSupport, null, infoSummary);
        final IRoom room = SummaryUtils.getRoom(persistentSupport, null, shift, infoSummary);

        final IProfessorship professorship = SummaryUtils.getProfessorship(persistentSupport,
                infoSummary);
        if (professorship != null) {
            final ISummary summary = executionCourse.createSummary(infoSummary.getTitle(), infoSummary
                    .getSummaryText(), infoSummary.getStudentsNumber(), infoSummary.getIsExtraLesson(),
                    professorship);
            shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                    .getSummaryHour().getTime(), room);
            return true;
        }

        final ITeacher teacher = SummaryUtils.getTeacher(persistentSupport, infoSummary);
        if (teacher != null) {
            final ISummary summary = executionCourse.createSummary(infoSummary.getTitle(), infoSummary
                    .getSummaryText(), infoSummary.getStudentsNumber(), infoSummary.getIsExtraLesson(),
                    teacher);
            shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                    .getSummaryHour().getTime(), room);
            return true;
        }

        String teacherName = infoSummary.getTeacherName();
        if (teacherName != null) {
            final ISummary summary = executionCourse.createSummary(infoSummary.getTitle(), infoSummary
                    .getSummaryText(), infoSummary.getStudentsNumber(), infoSummary.getIsExtraLesson(),
                    teacherName);
            shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                    .getSummaryHour().getTime(), room);
            return true;
        }

        throw new FenixServiceException("error.summary.no.teacher");
    }
}