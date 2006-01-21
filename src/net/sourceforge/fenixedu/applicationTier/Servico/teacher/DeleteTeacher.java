package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteTeacher extends Service {

    public Boolean run(Integer infoExecutionCourseCode, Integer teacherCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentProfessorship persistentProfessorship = persistentSupport.getIPersistentProfessorship();
        IPersistentSupportLesson supportLessonDAO = persistentSupport.getIPersistentSupportLesson();
        IPersistentShiftProfessorship shiftProfessorshipDAO = persistentSupport.getIPersistentShiftProfessorship();

        // note: removed the possibility for a responsible teacher to remove
        // from himself the professorship
        // (it was a feature that didnt make sense)
        
        Professorship professorshipToDelete = persistentProfessorship.readByTeacherAndExecutionCourse(
                teacherCode, infoExecutionCourseCode);

        List shiftProfessorshipList = shiftProfessorshipDAO.readByProfessorship(professorshipToDelete);
        List supportLessonList = supportLessonDAO.readByProfessorship(professorshipToDelete
                .getIdInternal());

        boolean hasCredits = false;
        boolean hasSupportLessons = supportLessonList != null && !supportLessonList.isEmpty();

        if (!shiftProfessorshipList.isEmpty()) {
            hasCredits = CollectionUtils.exists(shiftProfessorshipList, new Predicate() {

                public boolean evaluate(Object arg0) {
                    ShiftProfessorship shiftProfessorship = (ShiftProfessorship) arg0;
                    return shiftProfessorship.getPercentage() != null
                            && shiftProfessorship.getPercentage() != 0;
                }

            });
        }

        if (!hasCredits && !hasSupportLessons) {
            IPersistentSummary persistentSummary = persistentSupport.getIPersistentSummary();
            List summaryList = persistentSummary.readByTeacher(professorshipToDelete
                    .getExecutionCourse().getIdInternal(), professorshipToDelete.getTeacher()
                    .getTeacherNumber());
            if (summaryList != null && !summaryList.isEmpty()) {
                for (Iterator iterator = summaryList.iterator(); iterator.hasNext();) {
                    Summary summary = (Summary) iterator.next();
                    summary.setProfessorship(null);
                    summary.setKeyProfessorship(null);
                }
            }
            deleteProfessorship(persistentProfessorship, professorshipToDelete);

        } else {
            if (hasCredits) {
                throw new ExistingAssociatedCredits();
            } else if (hasSupportLessons) {
                throw new ExistingAssociatedSupportLessons();
            }
        }
        return Boolean.TRUE;
    }    

    private void deleteProfessorship(final IPersistentProfessorship persistentProfessorship,
            final Professorship professorshipToDelete) throws ExcepcaoPersistencia {

        if (professorshipToDelete.getAssociatedSummaries() != null)
            professorshipToDelete.getAssociatedSummaries().clear();
        if (professorshipToDelete.getSupportLessons() != null)
            professorshipToDelete.getSupportLessons().clear();

        if (professorshipToDelete.getAssociatedShiftProfessorship() != null)
            professorshipToDelete.getAssociatedShiftProfessorship().clear();

        if (professorshipToDelete.getExecutionCourse().getProfessorships() != null) {
            professorshipToDelete.getExecutionCourse().getProfessorships().remove(professorshipToDelete);
        }
        professorshipToDelete.setExecutionCourse(null);
        if (professorshipToDelete.getTeacher().getProfessorships() != null) {
            professorshipToDelete.getTeacher().getProfessorships().remove(professorshipToDelete);
        }
        professorshipToDelete.setTeacher(null);
        persistentProfessorship.deleteByOID(Professorship.class, professorshipToDelete.getIdInternal());
    }

    protected boolean canDeleteResponsibleFor() {
        return false;
    }

    private class ExistingAssociatedCredits extends FenixServiceException {

    }

    private class ExistingAssociatedSupportLessons extends FenixServiceException {

    }
}