/*
 * Created on 29/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Evaluation;
import Dominio.ExecutionCourse;
import Dominio.FinalEvaluation;
import Dominio.IBibliographicReference;
import Dominio.IEvaluation;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IGroupPropertiesExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.IShiftProfessorship;
import Dominio.ISite;
import Dominio.IStudent;
import Dominio.ISummary;
import Dominio.ISupportLesson;
import Dominio.ITurno;
import Dominio.gesdis.ICourseReport;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentGroupPropertiesExecutionCourse;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentShiftProfessorship;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentCourseReport;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;

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

    public MergeExecutionCourses() {
    }

    public void run(Integer executionCourseDestinationId, Integer executionCourseSourceId)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (executionCourseDestinationId.equals(executionCourseSourceId)) {
            throw new SourceAndDestinationAreTheSameException();
        }

       ISuportePersistente ps = SuportePersistenteOJB.getInstance();
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

    /**
     * @param destination
     * @param source
     * @param ps
     */
    private void copyEvaluationMethod(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {
        IPersistentEvaluationMethod persistentEvaluationMethod = ps.getIPersistentEvaluationMethod();
        IEvaluationMethod evaluationMethod = persistentEvaluationMethod.readByExecutionCourse(source);
        if (evaluationMethod != null) {
            persistentEvaluationMethod.delete(evaluationMethod);
        }

    }

    /**
     * @param destination
     * @param source
     * @param ps
     */
    private void copySummaries(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {
        IPersistentSummary persistentSummary = ps.getIPersistentSummary();
        List summaries = persistentSummary.readByExecutionCourse(source);
        Iterator iter = summaries.iterator();
        while (iter.hasNext()) {
            ISummary summary = (ISummary) iter.next();
            persistentSummary.simpleLockWrite(summary);
            summary.setExecutionCourse(destination);
        }

    }

    /**
     * @param destination
     * @param source
     * @param ps
     */
    private void copyCourseReport(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {
        IPersistentCourseReport persistentCourseReport = ps.getIPersistentCourseReport();
        ICourseReport courseReport = persistentCourseReport.readCourseReportByExecutionCourse(source);
        if (courseReport != null) {

            persistentCourseReport.delete(courseReport);
        }

    }

    /**
     * @param destination
     * @param source
     * @param ps
     */
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

    /**
     * @param executionCourse1
     * @param executionCourse2
     */
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

    /**
     * @param executionCourse1
     * @param executionCourse2
     */
    private void copyBibliography(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {
        IPersistentBibliographicReference persistentBibliographicReference = ps
                .getIPersistentBibliographicReference();
        List destinationBibliographicReferences = persistentBibliographicReference
                .readBibliographicReference(destination);
        if (destinationBibliographicReferences == null) {
            destinationBibliographicReferences = new ArrayList();
        }
        List sourceBibliographicReferences = persistentBibliographicReference
                .readBibliographicReference(source);
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
                    persistentBibliographicReference.delete(bibliographicReference);
                }

            }
        }

    }

    /**
     * @param executionCourse1
     * @param executionCourse2
     */
    private void copyShiftsAndLessons(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {
        ITurnoPersistente persistentShift = ps.getITurnoPersistente();
        // IAulaPersistente persistentLesson = ps.getIAulaPersistente();
        List sourceShifts = persistentShift.readByExecutionCourse(source);
        Iterator iter = sourceShifts.iterator();
        while (iter.hasNext()) {
            ITurno shift = (ITurno) iter.next();
            /*
             * List sourceLessons = shift.getAssociatedLessons(); Iterator
             * iterator = sourceLessons.iterator(); while (iterator.hasNext()) {
             * IAula lesson = (IAula) iterator.next();
             * persistentLesson.simpleLockWrite(lesson);
             * lesson.setDisciplinaExecucao(destination); }
             */
            persistentShift.simpleLockWrite(shift);
            shift.setDisciplinaExecucao(destination);
        }

    }

    /**
     * @param executionCourse1
     * @param executionCourse2
     */
    private void copyAttends(IExecutionCourse destination, IExecutionCourse source,
            ISuportePersistente ps) throws ExcepcaoPersistencia {

        List sourceAttends = source.getAttends();
        Map alreadyAttendingDestination = new HashMap();
        for (int i = 0; i < destination.getAttends().size(); i++) {
            IFrequenta attend = (IFrequenta) destination.getAttends().get(i);
            alreadyAttendingDestination.put(attend.getAluno().getNumber().toString(), attend);
        }
        Iterator iter = sourceAttends.iterator();
        while (iter.hasNext()) {
            IFrequenta attend = (IFrequenta) iter.next();
            if (!alreadyAttendingDestination.containsKey(attend.getAluno().getNumber().toString())) {
                attend.setDisciplinaExecucao(destination);
                try {
                    SuportePersistenteOJB.getInstance().currentBroker().store(attend);
                } catch (Exception e) {
                    throw new ExcepcaoPersistencia("Error storing attend", e);
                }

            }
        }

        // List destinationAttends =
        // persistentAttend.readByExecutionCourse(destination);
        // if (destinationAttends == null) {
        // destinationAttends = new ArrayList();
        // }
        // List sourceAttends = persistentAttend.readByExecutionCourse(source);
        // if (sourceAttends != null) {
        // Iterator iter = sourceAttends.iterator();
        // while (iter.hasNext()) {
        // IFrequenta attend = (IFrequenta) iter.next();
        // final IFrequenta attend2Compare = attend;
        // if (CollectionUtils.find(destinationAttends, new Predicate() {
        //
        // public boolean evaluate(Object arg0) {
        // IFrequenta frequenta = (IFrequenta) arg0;
        // if (frequenta.getAluno() == attend2Compare.getAluno()) {
        // return true;
        // }
        // return false;
        //
        // }
        // }) == null) {
        // persistentAttend.simpleLockWrite(attend);
        // attend.setDisciplinaExecucao(destination);
        //
        // } else {
        // persistentAttend.delete(attend);
        // }
        //
        // }
        // }
    }

    /**
     * @param executionCourse1
     * @param executionCourse2
     */
    private void copySites(IExecutionCourse destination, IExecutionCourse source, ISuportePersistente ps)
            throws ExcepcaoPersistencia {
        IPersistentSite persistentSite = ps.getIPersistentSite();
        ISite site = persistentSite.readByExecutionCourse(source);
        if (site != null) {

            persistentSite.delete(site);
        }

    }

    /**
     * @param executionCourse1
     * @param executionCourse2
     */
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
                    List summaryList = persistentSummary.readByTeacher(professorship.getExecutionCourse(), professorship.getTeacher());
                    if (summaryList != null && !summaryList.isEmpty()) {
                        for (Iterator iteratorSummaries = summaryList.iterator(); iteratorSummaries.hasNext(); ) {
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