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
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAnnouncement;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationMethod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 29/Nov/2003
 *  
 */
public class MergeExecutionCourses implements IService {

    public class SourceAndDestinationAreTheSameException extends FenixServiceException {
        private static final long serialVersionUID = 3761968254943244338L;
    }

    public class CONTAINS_STUDENT_PREDICATE implements Predicate {

        private IStudent student = null;

        public CONTAINS_STUDENT_PREDICATE(IStudent studentArg) {
            super();
            student = studentArg;
        }

        public boolean evaluate(Object arg0) {
            IStudent studentFromList = (IStudent) arg0;
            return (student.getIdInternal().equals(studentFromList.getIdInternal()));
        }

    }

    public void run(Integer executionCourseDestinationId, Integer executionCourseSourceId)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (executionCourseDestinationId.equals(executionCourseSourceId)) {
            throw new SourceAndDestinationAreTheSameException();
        }

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse persistentExecutionCourse = ps.getIPersistentExecutionCourse();
        IExecutionCourse destination;
        IExecutionCourse source;
        destination = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class,
                executionCourseDestinationId, true);
        source = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class,
                executionCourseSourceId);
        if (!isMergeAllowed(destination, source, ps)) {
            throw new InvalidArgumentsServiceException();
        }
        copyProfessorshipsAndResponsibleFors(destination, source, ps);
        copyAttends(destination, source, ps);
        copyBibliography(destination, source, ps);
        copyEvaluations(destination, source, ps);
        copySites(destination, source, ps);
        copyShiftsAndLessons(destination, source, ps);
        copyCourseReport(destination, source, ps);
        copyGroups(destination, source, ps);
        copySummaries(destination, source, ps);
        copyEvaluationMethod(destination, source, ps);

        destination.getAssociatedCurricularCourses().addAll(source.getAssociatedCurricularCourses());
        persistentExecutionCourse.deleteExecutionCourse(source);

    }

    private boolean isMergeAllowed(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) {

        boolean distributedTestAuthorization = false;

        IPersistentMetadata persistentMetadata = ps.getIPersistentMetadata();
        IPersistentDistributedTest persistentDistributedTest = ps.getIPersistentDistributedTest();
        try {
            List metadatas = persistentMetadata.readByExecutionCourse(source);
            List distributedTests = persistentDistributedTest.readByTestScopeObject(source);
            distributedTestAuthorization = (metadatas == null || metadatas.isEmpty())
                    && (distributedTests == null || distributedTests.isEmpty());

        } catch (ExcepcaoPersistencia e1) {
            // ignore

        }

        return destination != null && source != null
                && source.getExecutionPeriod().equals(destination.getExecutionPeriod())
                && source != destination && distributedTestAuthorization;
    }

    private void copyEvaluationMethod(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {
        IPersistentEvaluationMethod persistentEvaluationMethod = ps.getIPersistentEvaluationMethod();
        IEvaluationMethod evaluationMethod = persistentEvaluationMethod.readByIdExecutionCourse(source
                .getIdInternal());
        if (evaluationMethod != null) {
            evaluationMethod.getExecutionCourse().setEvaluationMethod(null);
            evaluationMethod.setExecutionCourse(null);
            persistentEvaluationMethod.deleteByOID(EvaluationMethod.class, evaluationMethod
                    .getIdInternal());
        }

    }

    private void copySummaries(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {
        IPersistentSummary persistentSummary = ps.getIPersistentSummary();
        List summaries = persistentSummary.readByExecutionCourse(source.getIdInternal());
        Iterator iter = summaries.iterator();
        while (iter.hasNext()) {
            ISummary summary = (ISummary) iter.next();
            persistentSummary.simpleLockWrite(summary);
            summary.setExecutionCourse(destination);
        }

    }

    private void copyCourseReport(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {
        IPersistentCourseReport persistentCourseReport = ps.getIPersistentCourseReport();
        ICourseReport courseReport = persistentCourseReport.readCourseReportByExecutionCourse(source
                .getIdInternal());
        if (courseReport != null) {
            courseReport.getExecutionCourse().setCourseReport(null);
            courseReport.setExecutionCourse(null);
            persistentCourseReport.deleteByOID(CourseReport.class, courseReport.getIdInternal());
        }

    }

    private void copyGroups(IExecutionCourse destination, IExecutionCourse source, ISuportePersistente ps)
            throws ExcepcaoPersistencia {
        IPersistentGroupPropertiesExecutionCourse persistentGroupPropertiesExecutionCourse = ps
                .getIPersistentGroupPropertiesExecutionCourse();
        List sourceGroupPropertiesExecutionCourseList = persistentGroupPropertiesExecutionCourse
                .readAllByExecutionCourse(source);
        Iterator iter = sourceGroupPropertiesExecutionCourseList.iterator();
        while (iter.hasNext()) {
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse) iter
                    .next();
            persistentGroupPropertiesExecutionCourse.simpleLockWrite(groupPropertiesExecutionCourse);
            groupPropertiesExecutionCourse.setExecutionCourse(destination);
            destination.addGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
            source.removeGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
        }

    }

    private void copyEvaluations(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {

        IPersistentObject persistentObject = ps.getIPersistentObject();

        List sourceEvaluations = source.getAssociatedEvaluations();
        if (sourceEvaluations != null && !sourceEvaluations.isEmpty()) {
            Iterator iter = sourceEvaluations.iterator();
            while (iter.hasNext()) {
                IEvaluation evaluation = (IEvaluation) iter.next();
                if (evaluation instanceof FinalEvaluation) {
                    persistentObject.deleteByOID(Evaluation.class, evaluation.getIdInternal());
                } else {
                    throw new InvalidArgumentsServiceException();
                }
            }
        }
    }

    private void copyBibliography(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {

        IPersistentBibliographicReference persistentBibliographicReference = ps
                .getIPersistentBibliographicReference();
        List destinationBibliographicReferences = persistentBibliographicReference
                .readBibliographicReference(destination.getIdInternal());
        if (destinationBibliographicReferences == null) {
            destinationBibliographicReferences = new ArrayList();
        }
        List sourceBibliographicReferences = persistentBibliographicReference
                .readBibliographicReference(source.getIdInternal());
        if (sourceBibliographicReferences != null) {
            Iterator iter = sourceBibliographicReferences.iterator();
            while (iter.hasNext()) {
                IBibliographicReference bibliographicReference = (IBibliographicReference) iter.next();
                final IBibliographicReference bibliographicReference2Compare = bibliographicReference;
                if (CollectionUtils.find(destinationBibliographicReferences, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        IBibliographicReference reference = (IBibliographicReference) arg0;
                        if (reference.getTitle() == bibliographicReference2Compare.getTitle()) {
                            return true;
                        }
                        return false;

                    }
                }) == null) {
                    persistentBibliographicReference.simpleLockWrite(bibliographicReference);
                    bibliographicReference.setExecutionCourse(destination);

                } else {
                    bibliographicReference.getExecutionCourse().getAssociatedBibliographicReferences()
                            .remove(bibliographicReference);
                    bibliographicReference.setExecutionCourse(null);
                    persistentBibliographicReference.deleteByOID(BibliographicReference.class,
                            bibliographicReference.getIdInternal());
                }

            }
        }

    }

    private void copyShiftsAndLessons(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {
        ITurnoPersistente persistentShift = ps.getITurnoPersistente();
        List sourceShifts = persistentShift.readByExecutionCourse(source);
        Iterator iter = sourceShifts.iterator();
        while (iter.hasNext()) {
            IShift shift = (IShift) iter.next();
            persistentShift.simpleLockWrite(shift);
            shift.setDisciplinaExecucao(destination);
        }

    }

    private void copyAttends(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {

        List sourceAttends = source.getAttends();
        Map alreadyAttendingDestination = new HashMap();
        for (int i = 0; i < destination.getAttends().size(); i++) {
            IAttends attend = (IAttends) destination.getAttends().get(i);
            alreadyAttendingDestination.put(attend.getAluno().getNumber().toString(), attend);
        }
        Iterator iter = sourceAttends.iterator();
        while (iter.hasNext()) {
            IAttends attend = (IAttends) iter.next();
            if (!alreadyAttendingDestination.containsKey(attend.getAluno().getNumber().toString())) {
                ps.getIFrequentaPersistente().simpleLockWrite(attend);
                attend.setDisciplinaExecucao(destination);
            }
        }
    }

    private void copySites(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente suportePersistente) throws ExcepcaoPersistencia {

        final ISite sourceSite = source.getSite();
        if (sourceSite != null) {
            IPersistentSite persistentSite = suportePersistente.getIPersistentSite();

            copySiteAnnouncements(suportePersistente, sourceSite.getAssociatedAnnouncements(),
                    destination.getSite());
            copySiteSections(suportePersistente, sourceSite.getAssociatedSections(), destination
                    .getSite());

            sourceSite.getExecutionCourse().setSite(null);
            sourceSite.setExecutionCourse(null);
            persistentSite.deleteByOID(Site.class, sourceSite.getIdInternal());
        }
    }

    private void copySiteSections(final ISuportePersistente suportePersistente,
            final List<ISection> associatedSections, final ISite destinationSite)
            throws ExcepcaoPersistencia {
        if (destinationSite != null) {
            IPersistentSection persistentSection = suportePersistente.getIPersistentSection();
            for (final ISection section : associatedSections) {
                persistentSection.simpleLockWrite(section);
                section.setSite(destinationSite);
            }
        }
    }

    private void copySiteAnnouncements(final ISuportePersistente suportePersistente,
            final List<IAnnouncement> associatedAnnouncements, final ISite destinationSite)
            throws ExcepcaoPersistencia {

        if (destinationSite != null) {
            IPersistentAnnouncement persistentAnnouncement = suportePersistente
                    .getIPersistentAnnouncement();
            for (final IAnnouncement announcement : associatedAnnouncements) {
                persistentAnnouncement.simpleLockWrite(announcement);
                announcement.setSite(destinationSite);
            }
        }
    }

    private void copyProfessorshipsAndResponsibleFors(IExecutionCourse destination,
            IExecutionCourse source, ISuportePersistente ps) throws ExcepcaoPersistencia {
        IPersistentProfessorship persistentProfessorship = ps.getIPersistentProfessorship();
        IPersistentShiftProfessorship persistentShiftProfessorship = ps
                .getIPersistentShiftProfessorship();
        IPersistentSupportLesson persistentSupportLesson = ps.getIPersistentSupportLesson();
        List destinationProfessorships = persistentProfessorship.readByExecutionCourse(destination);
        if (destinationProfessorships == null) {
            destinationProfessorships = new ArrayList();
        }
        List sourceProfessorships = persistentProfessorship.readByExecutionCourse(source);
        if (sourceProfessorships != null) {

            Iterator iter = sourceProfessorships.iterator();
            while (iter.hasNext()) {
                IProfessorship professorship = (IProfessorship) iter.next();
                final IProfessorship professorship2Compare = professorship;
                if (CollectionUtils.find(destinationProfessorships, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        IProfessorship prof = (IProfessorship) arg0;
                        if (prof.getTeacher() == professorship2Compare.getTeacher()) {
                            return true;
                        }
                        return false;

                    }
                }) == null) {
                    persistentProfessorship.simpleLockWrite(professorship);
                    professorship.setExecutionCourse(destination);

                } else {
                    List associatedShiftsProfessorships = professorship
                            .getAssociatedShiftProfessorship();
                    Iterator iterator = associatedShiftsProfessorships.iterator();

                    while (iterator.hasNext()) {
                        IShiftProfessorship shiftProfessorship = (IShiftProfessorship) iterator.next();
                        persistentShiftProfessorship.delete(shiftProfessorship);
                    }

                    List supportLessons = persistentSupportLesson.readByProfessorship(professorship);
                    iterator = supportLessons.iterator();
                    while (iterator.hasNext()) {
                        ISupportLesson supportLesson = (ISupportLesson) iterator.next();
                        persistentSupportLesson.delete(supportLesson);
                    }

                    IPersistentSummary persistentSummary = ps.getIPersistentSummary();
                    List summaryList = persistentSummary.readByTeacher(professorship
                            .getExecutionCourse().getIdInternal(), professorship.getTeacher()
                            .getTeacherNumber());
                    if (summaryList != null && !summaryList.isEmpty()) {
                        for (Iterator iteratorSummaries = summaryList.iterator(); iteratorSummaries
                                .hasNext();) {
                            ISummary summary = (ISummary) iteratorSummaries.next();
                            persistentSummary.simpleLockWrite(summary);
                            summary.setProfessorship(null);
                            summary.setKeyProfessorship(null);
                        }
                    }

                    persistentProfessorship.delete(professorship);
                }
            }
        }
        IPersistentResponsibleFor persistentResponsibleFor = ps.getIPersistentResponsibleFor();
        List destinationResponsibleFor = persistentResponsibleFor.readByExecutionCourse(destination);
        if (destinationResponsibleFor == null) {
            destinationResponsibleFor = new ArrayList();
        }
        List sourceResponsibleFor = persistentResponsibleFor.readByExecutionCourse(source);
        if (sourceResponsibleFor != null) {
            Iterator iter = sourceResponsibleFor.iterator();
            while (iter.hasNext()) {
                IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
                final IResponsibleFor responsibleFor2Compare = responsibleFor;
                if (CollectionUtils.find(destinationResponsibleFor, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        IResponsibleFor respons = (IResponsibleFor) arg0;
                        if (respons.getTeacher() == responsibleFor2Compare.getTeacher()) {
                            return true;
                        }
                        return false;

                    }
                }) == null) {
                    persistentResponsibleFor.simpleLockWrite(responsibleFor);
                    responsibleFor.setExecutionCourse(destination);

                } else {
                    persistentResponsibleFor.delete(responsibleFor);
                }
            }
        }
    }
}