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
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExportGrouping;
import net.sourceforge.fenixedu.domain.IFinalEvaluation;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluation;
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

        final IExecutionCourse executionCourseFrom = (IExecutionCourse) persistentExecutionCourse
                .readByOID(ExecutionCourse.class, executionCourseSourceId);
        if (executionCourseFrom == null) {
            throw new InvalidArgumentsServiceException();
        }

        final IExecutionCourse executionCourseTo = (IExecutionCourse) persistentExecutionCourse
                .readByOID(ExecutionCourse.class, executionCourseDestinationId, true);
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
            final IExecutionCourse executionCourseFrom, final IExecutionCourse executionCourseTo) {

        boolean distributedTestAuthorization = false;

        IPersistentMetadata persistentMetadata = persistentSupport.getIPersistentMetadata();
        IPersistentDistributedTest persistentDistributedTest = persistentSupport
                .getIPersistentDistributedTest();
        try {
            List metadatas = persistentMetadata.readByExecutionCourse(executionCourseFrom);
            List distributedTests = persistentDistributedTest.readByTestScope(executionCourseFrom
                    .getClass().getName(), executionCourseFrom.getIdInternal());
            distributedTestAuthorization = (metadatas == null || metadatas.isEmpty())
                    && (distributedTests == null || distributedTests.isEmpty());

        } catch (ExcepcaoPersistencia e) { // ignore
        }

        return executionCourseTo != null
                && executionCourseFrom != null
                && executionCourseFrom.getExecutionPeriod().equals(
                        executionCourseTo.getExecutionPeriod())
                && executionCourseFrom != executionCourseTo && distributedTestAuthorization;
    }

    private void removeEvaluationMethod(final ISuportePersistente persistentSupport,
            final IExecutionCourse executionCourseFrom) throws ExcepcaoPersistencia {
       
        final IEvaluationMethod evaluationMethod = executionCourseFrom.getEvaluationMethod();
        if (evaluationMethod != null) {
            evaluationMethod.delete();
        }
    }

    private void copySummaries(final ISuportePersistente persistentSupport,
            final IExecutionCourse executionCourseFrom, final IExecutionCourse executionCourseTo)
            throws ExcepcaoPersistencia {
        final List<ISummary> associatedSummaries = new ArrayList();
        associatedSummaries.addAll(executionCourseFrom.getAssociatedSummaries());
        for (final ISummary summary : associatedSummaries) {
            summary.setExecutionCourse(executionCourseTo);
        }
    }

    private void removeCourseReport(final ISuportePersistente persistentSupport,
            final IExecutionCourse executionCourseFrom) throws ExcepcaoPersistencia {
        
        final ICourseReport courseReport = executionCourseFrom.getCourseReport();
        if (courseReport != null) {
            courseReport.delete();
        }
    }

    private void copyGroupPropertiesExecutionCourse(final IExecutionCourse executionCourseFrom,
            final IExecutionCourse executionCourseTo) throws ExcepcaoPersistencia {
        final List<IExportGrouping> associatedGroupPropertiesExecutionCourse = new ArrayList();
        associatedGroupPropertiesExecutionCourse.addAll(executionCourseFrom
                .getExportGroupings());

        for (final IExportGrouping groupPropertiesExecutionCourse : associatedGroupPropertiesExecutionCourse) {
            groupPropertiesExecutionCourse.setExecutionCourse(executionCourseTo);
        }
    }

    private void removeEvaluations(final ISuportePersistente persistentSupport,
            final IExecutionCourse executionCourseFrom) throws ExcepcaoPersistencia,
            InvalidArgumentsServiceException {
        final List<IEvaluation> associatedEvaluations = new ArrayList();
        associatedEvaluations.addAll(executionCourseFrom.getAssociatedEvaluations());

        final IPersistentEvaluation persistentEvaluation = persistentSupport.getIPersistentEvaluation();
        for (final IEvaluation evaluation : associatedEvaluations) {
            if (evaluation instanceof IFinalEvaluation) {
                if (((IFinalEvaluation) evaluation).deleteFrom(executionCourseFrom)) {
                    persistentEvaluation.deleteByOID(Evaluation.class, evaluation.getIdInternal());
                }
            } else {
                throw new InvalidArgumentsServiceException();
            }

        }
    }

    private void copyBibliographicReference(final ISuportePersistente persistentSupport,
            final IExecutionCourse executionCourseFrom, final IExecutionCourse executionCourseTo)
            throws ExcepcaoPersistencia {

        final List<IBibliographicReference> notCopiedBibliographicReferences = ExecutionCourseUtils
                .copyBibliographicReference(executionCourseFrom, executionCourseTo);
        removeBibliographicReferences(persistentSupport, notCopiedBibliographicReferences);
    }

    private void removeBibliographicReferences(final ISuportePersistente persistentSupport,
            final List<IBibliographicReference> notCopiedBibliographicReferences)
            throws ExcepcaoPersistencia {
        
        for (final IBibliographicReference bibliographicReference : notCopiedBibliographicReferences) {
            bibliographicReference.delete();
        }
    }

    private void copyShifts(final IExecutionCourse executionCourseFrom,
            final IExecutionCourse executionCourseTo) throws ExcepcaoPersistencia {

        final List<IShift> associatedShifts = new ArrayList();
        associatedShifts.addAll(executionCourseFrom.getAssociatedShifts());

        for (final IShift shift : associatedShifts) {
            shift.setDisciplinaExecucao(executionCourseTo);
        }
    }

    private void copyAttends(final IExecutionCourse executionCourseFrom,
            final IExecutionCourse executionCourseTo) throws ExcepcaoPersistencia {

        final Iterator associatedAttendsFromDestination = executionCourseTo.getAttendsIterator();
        final Map alreadyAttendingDestination = new HashMap();
        while (associatedAttendsFromDestination.hasNext()) {
            IAttends attend = (IAttends) associatedAttendsFromDestination.next();
            alreadyAttendingDestination.put(attend.getAluno().getNumber().toString(), attend);
        }
        final List<IAttends> associatedAttendsFromSource = new ArrayList();
        associatedAttendsFromSource.addAll(executionCourseFrom.getAttends());
        for (final IAttends attend : associatedAttendsFromSource) {
            if (!alreadyAttendingDestination.containsKey(attend.getAluno().getNumber().toString())) {
                attend.setDisciplinaExecucao(executionCourseTo);
            }
        }
    }

    private void copySite(final ISuportePersistente persistentSupport,
            final IExecutionCourse executionCourseFrom, final IExecutionCourse executionCourseTo)
            throws ExcepcaoPersistencia {

        final ISite sourceSite = executionCourseFrom.getSite();
        if (sourceSite != null) {
            copySiteAnnouncements(executionCourseFrom.getSite(), executionCourseTo.getSite());
            copySiteSections(executionCourseFrom.getSite(), executionCourseTo.getSite());

            sourceSite.setExecutionCourse(null);
            final IPersistentSite persistentSite = persistentSupport.getIPersistentSite();
            persistentSite.deleteByOID(Site.class, sourceSite.getIdInternal());
        }
    }

    private void copySiteSections(final ISite siteFrom, final ISite siteTo) throws ExcepcaoPersistencia {
        if (siteTo != null) {
            final List<ISection> associatedSections = new ArrayList();
            associatedSections.addAll(siteFrom.getAssociatedSections());

            for (final ISection section : associatedSections) {
                section.setSite(siteTo);
            }
        }
    }

    private void copySiteAnnouncements(final ISite siteFrom, final ISite siteTo)
            throws ExcepcaoPersistencia {

        if (siteTo != null) {
            final List<IAnnouncement> associatedAnnouncements = new ArrayList();
            associatedAnnouncements.addAll(siteFrom.getAssociatedAnnouncements());

            for (final IAnnouncement announcement : associatedAnnouncements) {
                announcement.setSite(siteTo);
            }
        }
    }

    private void copyProfessorships(final ISuportePersistente persistentSupport,
            final IExecutionCourse executionCourseFrom, final IExecutionCourse executionCourseTo)
            throws ExcepcaoPersistencia {

        final List<IProfessorship> sourceExecutionCourseProfessorships = new ArrayList();
        sourceExecutionCourseProfessorships.addAll(executionCourseFrom.getProfessorships());
        for (final IProfessorship professorship : sourceExecutionCourseProfessorships) {
            if (canAddProfessorshipTo(executionCourseTo, professorship)) {
                professorship.setExecutionCourse(executionCourseTo);
            } else {
                removeProfessorship(persistentSupport, professorship);
            }
        }
    }

    private boolean canAddProfessorshipTo(final IExecutionCourse executionCourse,
            final IProfessorship professorshipToAdd) {
        final Iterator associatedProfessorships = executionCourse.getProfessorshipsIterator();
        while (associatedProfessorships.hasNext()) {
            IProfessorship professorship = (IProfessorship) associatedProfessorships.next();
            if (professorship.getTeacher().equals(professorshipToAdd.getTeacher())) {
                return false;
            }
        }
        return true;
    }

    private void removeProfessorship(final ISuportePersistente persistentSupport,
            final IProfessorship professorship) throws ExcepcaoPersistencia {

        deleteShiftProfessorships(persistentSupport, professorship);
        deleteSupportLessons(persistentSupport, professorship);
        dereferenceSummariesFrom(professorship);
        deleteProfessorship(persistentSupport, professorship);
    }

    private void dereferenceSummariesFrom(final IProfessorship professorship) {
        final Iterator associatedSummaries = professorship.getAssociatedSummariesIterator();
        while (associatedSummaries.hasNext()) {
            ISummary summary = (ISummary) associatedSummaries.next();
            summary.setProfessorship(null);
        }
    }

    private void deleteSupportLessons(final ISuportePersistente persistentSupport,
            final IProfessorship professorship) throws ExcepcaoPersistencia {
        final IPersistentSupportLesson persistentSupportLesson = persistentSupport
                .getIPersistentSupportLesson();
        final Iterator associatedSupportLessons = professorship.getSupportLessonsIterator();
        while (associatedSupportLessons.hasNext()) {
            ISupportLesson supportLesson = (ISupportLesson) associatedSupportLessons.next();
            supportLesson.setProfessorship(null);
            persistentSupportLesson.deleteByOID(SupportLesson.class, supportLesson.getIdInternal());
        }
    }

    private void deleteShiftProfessorships(final ISuportePersistente persistentSupport,
            final IProfessorship professorship) throws ExcepcaoPersistencia {
        final IPersistentShiftProfessorship persistentShiftProfessorship = persistentSupport
                .getIPersistentShiftProfessorship();
        final Iterator associatedShifProfessorships = professorship
                .getAssociatedShiftProfessorshipIterator();
        while (associatedShifProfessorships.hasNext()) {
            IShiftProfessorship shiftProfessorship = (IShiftProfessorship) associatedShifProfessorships
                    .next();
            shiftProfessorship.setProfessorship(null);
            shiftProfessorship.setShift(null);
            persistentShiftProfessorship.deleteByOID(ShiftProfessorship.class, shiftProfessorship
                    .getIdInternal());
        }
    }

    private void deleteProfessorship(final ISuportePersistente persistentSupport,
            final IProfessorship professorshipToDelete) throws ExcepcaoPersistencia {                
        professorshipToDelete.delete();
    }

}