/*
 * Created on 21/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.summary.SummaryUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * @author Susana Fernandes 21/Jul/2003 fenix-head
 *         ServidorAplicacao.Servico.teacher
 * 
 * @author jdnf and mrsp 21/Jul/2005
 * 
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

        final ISummary summary = (ISummary) persistentSummary.readByOID(Summary.class, infoSummary
                .getIdInternal());
        final IShift shift = SummaryUtils.getShift(persistentSupport, summary, infoSummary);
        final IRoom room = SummaryUtils.getRoom(persistentSupport, summary, shift, infoSummary);

        shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                .getSummaryHour().getTime(), room);

        final IProfessorship professorship = SummaryUtils.getProfessorship(persistentSupport, infoSummary);
        if (professorship != null) {
            summary.edit(infoSummary.getTitle(), infoSummary.getSummaryText(), infoSummary
                    .getStudentsNumber(), infoSummary.getIsExtraLesson(), professorship);
            return;
        }

        final ITeacher teacher = SummaryUtils.getTeacher(persistentSupport, infoSummary);
        if (teacher != null) {
            summary.edit(infoSummary.getTitle(), infoSummary.getSummaryText(), infoSummary
                    .getStudentsNumber(), infoSummary.getIsExtraLesson(), teacher);
            return;
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