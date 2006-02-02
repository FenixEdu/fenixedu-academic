/*
 * Created on 29/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt"> João Mota </a> 29/Nov/2003
 * 
 */
public class MergeExecutionCourses extends Service {

    public class SourceAndDestinationAreTheSameException extends FenixServiceException {
        private static final long serialVersionUID = 3761968254943244338L;
    }

    public class DuplicateShiftNameException extends FenixServiceException {
        private static final long serialVersionUID = 3761968254943244338L;
    }

    public void run(Integer executionCourseDestinationId, Integer executionCourseSourceId)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (executionCourseDestinationId.equals(executionCourseSourceId)) {
            throw new SourceAndDestinationAreTheSameException();
        }

        final ExecutionCourse executionCourseFrom = (ExecutionCourse) persistentObject
                .readByOID(ExecutionCourse.class, executionCourseSourceId);
        if (executionCourseFrom == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ExecutionCourse executionCourseTo = (ExecutionCourse) persistentObject
                .readByOID(ExecutionCourse.class, executionCourseDestinationId);
        if (executionCourseTo == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (!isMergeAllowed(persistentSupport, executionCourseFrom, executionCourseTo)) {
            throw new InvalidArgumentsServiceException();
        }

        if (haveShiftsWithSameName(executionCourseFrom, executionCourseTo)) {
            throw new DuplicateShiftNameException();
        }

        copyProfessorships(persistentSupport, executionCourseFrom, executionCourseTo);
        copyAttends(executionCourseFrom, executionCourseTo);
        copyBibliographicReference(persistentSupport, executionCourseFrom, executionCourseTo);
        if (executionCourseFrom.getEvaluationMethod() != null) {
            executionCourseFrom.getEvaluationMethod().delete();
        }
        if (executionCourseFrom.getCourseReport() != null) {
            executionCourseFrom.getCourseReport().delete();
        }
        copySummaries(persistentSupport, executionCourseFrom, executionCourseTo);
        copyGroupPropertiesExecutionCourse(executionCourseFrom, executionCourseTo);
        copySite(persistentSupport, executionCourseFrom, executionCourseTo);
        copyShifts(executionCourseFrom, executionCourseTo);
        removeEvaluations(persistentSupport, executionCourseFrom, executionCourseTo);

        executionCourseTo.getAssociatedCurricularCourses().addAll(executionCourseFrom.getAssociatedCurricularCourses());

        executionCourseFrom.delete();
    }

    private boolean haveShiftsWithSameName(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        final Set<String> shiftNames = new HashSet<String>();
        for (final Shift shift : executionCourseFrom.getAssociatedShifts()) {
            shiftNames.add(shift.getNome());
        }
        for (final Shift shift : executionCourseTo.getAssociatedShifts()) {
            if (shiftNames.contains(shift.getNome())) {
                return true;
            }
        }
        return false;
    }

    private boolean isMergeAllowed(final ISuportePersistente persistentSupport,
            final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) throws ExcepcaoPersistencia {

        boolean distributedTestAuthorization = false;

        IPersistentMetadata persistentMetadata = persistentSupport.getIPersistentMetadata();
        IPersistentDistributedTest persistentDistributedTest = persistentSupport
                .getIPersistentDistributedTest();

            List metadatas = persistentMetadata.readByExecutionCourse(executionCourseFrom);
            List distributedTests = persistentDistributedTest.readByTestScope(executionCourseFrom
                    .getClass().getName(), executionCourseFrom.getIdInternal());
            distributedTestAuthorization = (metadatas == null || metadatas.isEmpty())
                    && (distributedTests == null || distributedTests.isEmpty());

        return executionCourseTo != null
                && executionCourseFrom != null
                && executionCourseFrom.getExecutionPeriod().equals(
                        executionCourseTo.getExecutionPeriod())
                && executionCourseFrom != executionCourseTo && distributedTestAuthorization;
    }

    private void copySummaries(final ISuportePersistente persistentSupport,
            final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws ExcepcaoPersistencia {
        final List<Summary> associatedSummaries = new ArrayList();
        associatedSummaries.addAll(executionCourseFrom.getAssociatedSummaries());
        for (final Summary summary : associatedSummaries) {
            summary.setExecutionCourse(executionCourseTo);
        }
    }

    private void copyGroupPropertiesExecutionCourse(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) throws ExcepcaoPersistencia {
        final List<ExportGrouping> associatedGroupPropertiesExecutionCourse = new ArrayList();
        associatedGroupPropertiesExecutionCourse.addAll(executionCourseFrom
                .getExportGroupings());

        for (final ExportGrouping groupPropertiesExecutionCourse : associatedGroupPropertiesExecutionCourse) {
            groupPropertiesExecutionCourse.setExecutionCourse(executionCourseTo);
        }
    }

    private void removeEvaluations(final ISuportePersistente persistentSupport,
            final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws ExcepcaoPersistencia, FenixServiceException {
        while (!executionCourseFrom.getAssociatedEvaluations().isEmpty()) {
            final Evaluation evaluation = executionCourseFrom.getAssociatedEvaluations().get(0);
            if (evaluation instanceof FinalEvaluation) {
                final FinalEvaluation finalEvaluationFrom = (FinalEvaluation) evaluation;
                if (finalEvaluationFrom.hasAnyMarks()) {
                    throw new FenixServiceException("Cannot merge execution courses: marks exist for final evaluation.");
                } else {
                    finalEvaluationFrom.delete();
                }
            } else {
                executionCourseTo.getAssociatedEvaluations().add(evaluation);
                executionCourseFrom.getAssociatedEvaluations().remove(evaluation);
            }
        }
    }

    private void copyBibliographicReference(final ISuportePersistente persistentSupport,
            final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws ExcepcaoPersistencia {
        for (; !executionCourseFrom.getAssociatedBibliographicReferences().isEmpty();
            executionCourseTo.getAssociatedBibliographicReferences().add(executionCourseFrom.getAssociatedBibliographicReferences().get(0)));
    }

    private void copyShifts(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) throws ExcepcaoPersistencia {

        final List<Shift> associatedShifts = new ArrayList();
        associatedShifts.addAll(executionCourseFrom.getAssociatedShifts());

        for (final Shift shift : associatedShifts) {
            shift.setDisciplinaExecucao(executionCourseTo);
        }
    }

    private void copyAttends(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) throws ExcepcaoPersistencia, FenixServiceException {
        while (!executionCourseFrom.getAttends().isEmpty()) {
            final Attends attends = executionCourseFrom.getAttends().get(0);
            final Attends otherAttends = executionCourseTo.getAttendsByStudent(attends.getAluno());
            if (otherAttends == null) {
                attends.setDisciplinaExecucao(executionCourseTo);
            } else {
                if (attends.hasEnrolment() && !otherAttends.hasAluno()) {
                    otherAttends.setEnrolment(attends.getEnrolment());
                } else if (otherAttends.hasEnrolment() && attends.hasAluno()) {
                    throw new FenixServiceException("Unable to merge execution courses. Student " + attends.getAluno().getNumber() + " has an enrolment in both.");
                //} else {
                    // all is ok
                }
                attends.delete();
            }
        }

        final Iterator associatedAttendsFromDestination = executionCourseTo.getAttendsIterator();
        final Map alreadyAttendingDestination = new HashMap();
        while (associatedAttendsFromDestination.hasNext()) {
            Attends attend = (Attends) associatedAttendsFromDestination.next();
            alreadyAttendingDestination.put(attend.getAluno().getNumber().toString(), attend);
        }
        final List<Attends> associatedAttendsFromSource = new ArrayList();
        associatedAttendsFromSource.addAll(executionCourseFrom.getAttends());
        for (final Attends attend : associatedAttendsFromSource) {
            if (!alreadyAttendingDestination.containsKey(attend.getAluno().getNumber().toString())) {
                attend.setDisciplinaExecucao(executionCourseTo);
            }
        }
    }

    private void copySite(final ISuportePersistente persistentSupport,
            final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws ExcepcaoPersistencia {

        final Site sourceSite = executionCourseFrom.getSite();
        if (sourceSite != null) {
            copySiteAnnouncements(executionCourseFrom.getSite(), executionCourseTo.getSite());
            copySiteSections(executionCourseFrom.getSite(), executionCourseTo.getSite());

            sourceSite.setExecutionCourse(null);
            final IPersistentSite persistentSite = persistentSupport.getIPersistentSite();
            persistentSite.deleteByOID(Site.class, sourceSite.getIdInternal());
        }
    }

    private void copySiteSections(final Site siteFrom, final Site siteTo) throws ExcepcaoPersistencia {
        if (siteTo != null) {
            final List<Section> associatedSections = new ArrayList();
            associatedSections.addAll(siteFrom.getAssociatedSections());

            for (final Section section : associatedSections) {
                section.setSite(siteTo);
            }
        }
    }

    private void copySiteAnnouncements(final Site siteFrom, final Site siteTo)
            throws ExcepcaoPersistencia {

        if (siteTo != null) {
            final List<Announcement> associatedAnnouncements = new ArrayList();
            associatedAnnouncements.addAll(siteFrom.getAssociatedAnnouncements());

            for (final Announcement announcement : associatedAnnouncements) {
                announcement.setSite(siteTo);
            }
        }
    }

    private void copyProfessorships(final ISuportePersistente persistentSupport,
            final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws ExcepcaoPersistencia {
        for (;!executionCourseFrom.getProfessorships().isEmpty();) {
            final Professorship professorship = executionCourseFrom.getProfessorships().get(0);
            if (canAddProfessorshipTo(executionCourseTo, professorship)) {
                professorship.setExecutionCourse(executionCourseTo);
            } else {
                professorship.delete();
            }
        }
    }

    private boolean canAddProfessorshipTo(final ExecutionCourse executionCourse,
            final Professorship professorshipToAdd) {
        final Iterator associatedProfessorships = executionCourse.getProfessorshipsIterator();
        while (associatedProfessorships.hasNext()) {
            Professorship professorship = (Professorship) associatedProfessorships.next();
            if (professorship.getTeacher().equals(professorshipToAdd.getTeacher())) {
                return false;
            }
        }
        return true;
    }

    private void removeProfessorship(final ISuportePersistente persistentSupport,
            final Professorship professorship) throws ExcepcaoPersistencia {

        deleteShiftProfessorships(persistentSupport, professorship);
        deleteSupportLessons(persistentSupport, professorship);
        dereferenceSummariesFrom(professorship);
        deleteProfessorship(persistentSupport, professorship);
    }

    private void dereferenceSummariesFrom(final Professorship professorship) {
        final Iterator associatedSummaries = professorship.getAssociatedSummariesIterator();
        while (associatedSummaries.hasNext()) {
            Summary summary = (Summary) associatedSummaries.next();
            summary.setProfessorship(null);
        }
    }

    private void deleteSupportLessons(final ISuportePersistente persistentSupport,
            final Professorship professorship) throws ExcepcaoPersistencia {
        final IPersistentSupportLesson persistentSupportLesson = persistentSupport
                .getIPersistentSupportLesson();
        final Iterator associatedSupportLessons = professorship.getSupportLessonsIterator();
        while (associatedSupportLessons.hasNext()) {
            SupportLesson supportLesson = (SupportLesson) associatedSupportLessons.next();
            supportLesson.setProfessorship(null);
            persistentSupportLesson.deleteByOID(SupportLesson.class, supportLesson.getIdInternal());
        }
    }

    private void deleteShiftProfessorships(final ISuportePersistente persistentSupport,
            final Professorship professorship) throws ExcepcaoPersistencia {
        final IPersistentShiftProfessorship persistentShiftProfessorship = persistentSupport
                .getIPersistentShiftProfessorship();
        final Iterator associatedShifProfessorships = professorship
                .getAssociatedShiftProfessorshipIterator();
        while (associatedShifProfessorships.hasNext()) {
            ShiftProfessorship shiftProfessorship = (ShiftProfessorship) associatedShifProfessorships
                    .next();
            shiftProfessorship.setProfessorship(null);
            shiftProfessorship.setShift(null);
            persistentShiftProfessorship.deleteByOID(ShiftProfessorship.class, shiftProfessorship
                    .getIdInternal());
        }
    }

    private void deleteProfessorship(final ISuportePersistente persistentSupport,
            final Professorship professorshipToDelete) throws ExcepcaoPersistencia {                
        professorshipToDelete.delete();
    }

}