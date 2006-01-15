/*
 * Created on 29/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.ExecutionCourseUtils;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
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
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt"> João Mota </a> 29/Nov/2003
 * 
 */
public class MergeExecutionCourses implements IService {

    public class SourceAndDestinationAreTheSameException extends FenixServiceException {
        private static final long serialVersionUID = 3761968254943244338L;
    }

    public void run(Integer executionCourseDestinationId, Integer executionCourseSourceId)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (executionCourseDestinationId.equals(executionCourseSourceId)) {
            throw new SourceAndDestinationAreTheSameException();
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final ExecutionCourse executionCourseFrom = (ExecutionCourse) persistentExecutionCourse
                .readByOID(ExecutionCourse.class, executionCourseSourceId);
        if (executionCourseFrom == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ExecutionCourse executionCourseTo = (ExecutionCourse) persistentExecutionCourse
                .readByOID(ExecutionCourse.class, executionCourseDestinationId);
        if (executionCourseTo == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (!isMergeAllowed(persistentSupport, executionCourseFrom, executionCourseTo)) {
            throw new InvalidArgumentsServiceException();
        }
        copyProfessorships(persistentSupport, executionCourseFrom, executionCourseTo);
        copyAttends(executionCourseFrom, executionCourseTo);
        copyBibliographicReference(persistentSupport, executionCourseFrom, executionCourseTo);
        removeEvaluationMethod(persistentSupport, executionCourseFrom);
        removeCourseReport(persistentSupport, executionCourseFrom);
        copySummaries(persistentSupport, executionCourseFrom, executionCourseTo);
        copyGroupPropertiesExecutionCourse(executionCourseFrom, executionCourseTo);
        copySite(persistentSupport, executionCourseFrom, executionCourseTo);
        copyShifts(executionCourseFrom, executionCourseTo);
        removeEvaluations(persistentSupport, executionCourseFrom);

        executionCourseTo.getAssociatedCurricularCourses().addAll(
                executionCourseFrom.getAssociatedCurricularCourses());

        List executionCourseID = new ArrayList();
        executionCourseID.add(executionCourseFrom.getIdInternal());

        DeleteExecutionCourses deleteExecutionCourses = new DeleteExecutionCourses();
        deleteExecutionCourses.run(executionCourseID);
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

    private void removeEvaluationMethod(final ISuportePersistente persistentSupport,
            final ExecutionCourse executionCourseFrom) throws ExcepcaoPersistencia {
       
        final EvaluationMethod evaluationMethod = executionCourseFrom.getEvaluationMethod();
        if (evaluationMethod != null) {
            evaluationMethod.delete();
        }
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

    private void removeCourseReport(final ISuportePersistente persistentSupport,
            final ExecutionCourse executionCourseFrom) throws ExcepcaoPersistencia {
        
        final CourseReport courseReport = executionCourseFrom.getCourseReport();
        if (courseReport != null) {
            courseReport.delete();
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
            final ExecutionCourse executionCourseFrom) throws ExcepcaoPersistencia,
            InvalidArgumentsServiceException {
        final List<Evaluation> associatedEvaluations = new ArrayList();
        associatedEvaluations.addAll(executionCourseFrom.getAssociatedEvaluations());

        for (final Evaluation evaluation : associatedEvaluations) {
            if (evaluation instanceof FinalEvaluation) {
                if (((FinalEvaluation) evaluation).deleteFrom(executionCourseFrom)) {
                    persistentSupport.getIPersistentObject().deleteByOID(Evaluation.class, evaluation.getIdInternal());
                }
            } else {
                throw new InvalidArgumentsServiceException();
            }

        }
    }

    private void copyBibliographicReference(final ISuportePersistente persistentSupport,
            final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws ExcepcaoPersistencia {

        final List<BibliographicReference> notCopiedBibliographicReferences = ExecutionCourseUtils
                .copyBibliographicReference(executionCourseFrom, executionCourseTo);
        removeBibliographicReferences(persistentSupport, notCopiedBibliographicReferences);
    }

    private void removeBibliographicReferences(final ISuportePersistente persistentSupport,
            final List<BibliographicReference> notCopiedBibliographicReferences)
            throws ExcepcaoPersistencia {
        
        for (final BibliographicReference bibliographicReference : notCopiedBibliographicReferences) {
            bibliographicReference.delete();
        }
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
            final ExecutionCourse executionCourseTo) throws ExcepcaoPersistencia {

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

        final List<Professorship> sourceExecutionCourseProfessorships = new ArrayList();
        sourceExecutionCourseProfessorships.addAll(executionCourseFrom.getProfessorships());
        for (final Professorship professorship : sourceExecutionCourseProfessorships) {
            if (canAddProfessorshipTo(executionCourseTo, professorship)) {
                professorship.setExecutionCourse(executionCourseTo);
            } else {
                removeProfessorship(persistentSupport, professorship);
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