/*
 * Created on 29/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ExecutionCourse;
import Dominio.IAula;
import Dominio.IBibliographicReference;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.IShiftProfessorship;
import Dominio.ISite;
import Dominio.ISummary;
import Dominio.ISupportLesson;
import Dominio.ITurno;
import Dominio.gesdis.ICourseReport;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentEvaluationExecutionCourse;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentMetadata;
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
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a> 29/Nov/2003
 *  
 */
public class MergeExecutionCourses implements IServico
{

    /**
	 *  
	 */
    public MergeExecutionCourses()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {

        return "MergeExecutionCourses";
    }

    private static MergeExecutionCourses _servico = new MergeExecutionCourses();
    /**
	 * The singleton access method of this class.
	 */
    public static MergeExecutionCourses getService()
    {
        return _servico;
    }

    public void run(Integer executionCourseDestinationId, Integer executionCourseSourceId)
        throws FenixServiceException
    {

        try
        {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = ps.getIPersistentExecutionCourse();
            IExecutionCourse destination = new ExecutionCourse(executionCourseDestinationId);
            IExecutionCourse source = new ExecutionCourse(executionCourseSourceId);
            destination = (IExecutionCourse) persistentExecutionCourse.readByOId(destination, true);
            source = (IExecutionCourse) persistentExecutionCourse.readByOId(source, false);
            if (!isMergeAllowed(destination, source, ps))
            {
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
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

    }

    private boolean isMergeAllowed(
        IExecutionCourse destination,
        IExecutionCourse source,
        ISuportePersistente ps)
    {

        boolean distributedTestAuthorization = false;
//        boolean slideAuthorization = false;
//        IFileSuport fileSuport = FileSuport.getInstance();

//        slideAuthorization = fileSuport.getDirectorySize(source.getSlideName()) <= 0;

        IPersistentMetadata persistentMetadata = ps.getIPersistentMetadata();
        IPersistentDistributedTest persistentDistributedTest = ps.getIPersistentDistributedTest();
        try
        {
            List metadatas = persistentMetadata.readByExecutionCourse(source);
            List distributedTests = persistentDistributedTest.readByTestScopeObject(source);
            distributedTestAuthorization =
                (metadatas == null || metadatas.isEmpty())
                    && (distributedTests == null || distributedTests.isEmpty());

        }
        catch (ExcepcaoPersistencia e1)
        {
            //ignore
        }

        return destination != null
            && source != null
            && source.getExecutionPeriod().equals(destination.getExecutionPeriod())
            && source != destination
            && distributedTestAuthorization;
//            && slideAuthorization;
    }

    /**
	 * @param destination
	 * @param source
	 * @param ps
	 */
    private void copyEvaluationMethod(
        IExecutionCourse destination,
        IExecutionCourse source,
        ISuportePersistente ps)
        throws ExcepcaoPersistencia
    {
        IPersistentEvaluationMethod persistentEvaluationMethod = ps.getIPersistentEvaluationMethod();
        IEvaluationMethod evaluationMethod = persistentEvaluationMethod.readByExecutionCourse(source);
        if (evaluationMethod != null)
        {
            persistentEvaluationMethod.delete(evaluationMethod);
        }

    }

    /**
	 * @param destination
	 * @param source
	 * @param ps
	 */
    private void copySummaries(
        IExecutionCourse destination,
        IExecutionCourse source,
        ISuportePersistente ps)
        throws ExcepcaoPersistencia
    {
        IPersistentSummary persistentSummary = ps.getIPersistentSummary();
        List summaries = persistentSummary.readByExecutionCourse(source);
        Iterator iter = summaries.iterator();
        while (iter.hasNext())
        {
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
    private void copyCourseReport(
        IExecutionCourse destination,
        IExecutionCourse source,
        ISuportePersistente ps)
        throws ExcepcaoPersistencia
    {
        IPersistentCourseReport persistentCourseReport = ps.getIPersistentCourseReport();
        ICourseReport courseReport = persistentCourseReport.readCourseReportByExecutionCourse(source);
        if (courseReport!=null) {
            
        persistentCourseReport.delete(courseReport);
        }

    }

    /**
	 * @param destination
	 * @param source
	 * @param ps
	 */
    private void copyGroups(
        IExecutionCourse destination,
        IExecutionCourse source,
        ISuportePersistente ps)
        throws ExcepcaoPersistencia
    {
        IPersistentGroupProperties persistentGroupProperties = ps.getIPersistentGroupProperties();
        List sourceGroupPropertiesList =
            persistentGroupProperties.readAllGroupPropertiesByExecutionCourse(source);
        Iterator iter = sourceGroupPropertiesList.iterator();
        while (iter.hasNext())
        {
            IGroupProperties groupProperties = (IGroupProperties) iter.next();
            persistentGroupProperties.simpleLockWrite(groupProperties);
            groupProperties.setExecutionCourse(destination);
        }

    }

    /**
	 * @param executionCourse1
	 * @param executionCourse2
	 */
    private void copyEvaluations(
        IExecutionCourse destination,
        IExecutionCourse source,
        ISuportePersistente ps)
        throws ExcepcaoPersistencia, InvalidArgumentsServiceException
    {

        IPersistentEvaluationExecutionCourse persistentEvaluationExecutionCourse =
            ps.getIPersistentEvaluationExecutionCourse();
        List destinationEvaluations =
            persistentEvaluationExecutionCourse.readByExecutionCourse(destination);
        if (destinationEvaluations == null)
        {
            destinationEvaluations = new ArrayList();
        }
        List sourceEvaluations = persistentEvaluationExecutionCourse.readByExecutionCourse(source);
        if (sourceEvaluations != null && !sourceEvaluations.isEmpty())
        {
            throw new InvalidArgumentsServiceException();
//            Iterator iter = sourceEvaluations.iterator();
//            while (iter.hasNext())
//            {
//                IEvalutionExecutionCourse evaluationExecutionCourse =
//                    (IEvalutionExecutionCourse) iter.next();
//                final IEvalutionExecutionCourse evaluationExecutionCourse2Compare =
//                    evaluationExecutionCourse;
//                if (CollectionUtils.find(destinationEvaluations, new Predicate()
//                {
//
//                    public boolean evaluate(Object arg0)
//                    {
//                        IEvalutionExecutionCourse evaluationExecutionCourseArg =
//                            (IEvalutionExecutionCourse) arg0;
//                            if (evaluationExecutionCourseArg.getEvaluation()
//                                == evaluationExecutionCourse2Compare.getEvaluation())
//                        {
//                            return true; }
//                        else
//                        {
//                            return false; }
//                    }
//                }) == null)
//                {
//                    persistentEvaluationExecutionCourse.simpleLockWrite(evaluationExecutionCourse);
//                    evaluationExecutionCourse.setExecutionCourse(destination);
//
//                }
//                else
//                {
//                    if (evaluationExecutionCourse.getEvaluation().getAssociatedExecutionCourses().size()
//                        == 1)
//                    {
//                        persistentEvaluationExecutionCourse.delete(
//                            evaluationExecutionCourse.getEvaluation());
//                    }
//                    persistentEvaluationExecutionCourse.delete(evaluationExecutionCourse);
//
//                }
//            }
        }

    }

    /**
	 * @param executionCourse1
	 * @param executionCourse2
	 */
    private void copyBibliography(
        IExecutionCourse destination,
        IExecutionCourse source,
        ISuportePersistente ps)
        throws ExcepcaoPersistencia
    {
        IPersistentBibliographicReference persistentBibliographicReference =
            ps.getIPersistentBibliographicReference();
        List destinationBibliographicReferences =
            persistentBibliographicReference.readBibliographicReference(destination);
        if (destinationBibliographicReferences == null)
        {
            destinationBibliographicReferences = new ArrayList();
        }
        List sourceBibliographicReferences =
            persistentBibliographicReference.readBibliographicReference(source);
        if (sourceBibliographicReferences != null)
        {
            Iterator iter = sourceBibliographicReferences.iterator();
            while (iter.hasNext())
            {
                IBibliographicReference bibliographicReference = (IBibliographicReference) iter.next();
                final IBibliographicReference bibliographicReference2Compare = bibliographicReference;
                if (CollectionUtils.find(destinationBibliographicReferences, new Predicate()
                {

                    public boolean evaluate(Object arg0)
                    {
                        IBibliographicReference reference = (IBibliographicReference) arg0;
                            if (reference.getTitle() == bibliographicReference2Compare.getTitle())
                        {
                            return true; }
                        else
                        {
                            return false; }
                    }
                }) == null)
                {
                    persistentBibliographicReference.simpleLockWrite(bibliographicReference);
                    bibliographicReference.setExecutionCourse(destination);

                }
                else
                {
                    persistentBibliographicReference.delete(bibliographicReference);
                }

            }
        }

    }

    /**
	 * @param executionCourse1
	 * @param executionCourse2
	 */
    private void copyShiftsAndLessons(
        IExecutionCourse destination,
        IExecutionCourse source,
        ISuportePersistente ps)
        throws ExcepcaoPersistencia
    {
        ITurnoPersistente persistentShift = ps.getITurnoPersistente();
        IAulaPersistente persistentLesson = ps.getIAulaPersistente();
        List sourceShifts = persistentShift.readByExecutionCourse(source);
        Iterator iter = sourceShifts.iterator();
        while (iter.hasNext())
        {
            ITurno shift = (ITurno) iter.next();
            List sourceLessons = shift.getAssociatedLessons();
            Iterator iterator = sourceLessons.iterator();
            while (iterator.hasNext())
            {
                IAula lesson = (IAula) iterator.next();
                persistentLesson.simpleLockWrite(lesson);
                lesson.setDisciplinaExecucao(destination);
            }
            persistentShift.simpleLockWrite(shift);
            shift.setDisciplinaExecucao(destination);
        }

    }

    /**
	 * @param executionCourse1
	 * @param executionCourse2
	 */
    private void copyAttends(
        IExecutionCourse destination,
        IExecutionCourse source,
        ISuportePersistente ps)
        throws ExcepcaoPersistencia
    {
        IFrequentaPersistente persistentAttend = ps.getIFrequentaPersistente();
        List destinationAttends = persistentAttend.readByExecutionCourse(destination);
        if (destinationAttends == null)
        {
            destinationAttends = new ArrayList();
        }
        List sourceAttends = persistentAttend.readByExecutionCourse(source);
        if (sourceAttends != null)
        {
            Iterator iter = sourceAttends.iterator();
            while (iter.hasNext())
            {
                IFrequenta attend = (IFrequenta) iter.next();
                final IFrequenta attend2Compare = attend;
                if (CollectionUtils.find(destinationAttends, new Predicate()
                {

                    public boolean evaluate(Object arg0)
                    {
                        IFrequenta frequenta = (IFrequenta) arg0;
                            if (frequenta.getAluno() == attend2Compare.getAluno())
                        {
                            return true; }
                        else
                        {
                            return false; }
                    }
                }) == null)
                {
                    persistentAttend.simpleLockWrite(attend);
                    attend.setDisciplinaExecucao(destination);

                }
                else
                {
                    persistentAttend.delete(attend);
                }

            }
        }

    }

    /**
	 * @param executionCourse1
	 * @param executionCourse2
	 */
    private void copySites(
        IExecutionCourse destination,
        IExecutionCourse source,
        ISuportePersistente ps)
        throws ExcepcaoPersistencia
    {
        IPersistentSite persistentSite = ps.getIPersistentSite();
        ISite site = persistentSite.readByExecutionCourse(source);
        if (site!=null) {
            
        persistentSite.delete(site);
        }

    }

    /**
	 * @param executionCourse1
	 * @param executionCourse2
	 */
    private void copyProfessorshipsAndResponsibleFors(
        IExecutionCourse destination,
        IExecutionCourse source,
        ISuportePersistente ps)
        throws ExcepcaoPersistencia
    {
        IPersistentProfessorship persistentProfessorship = ps.getIPersistentProfessorship();
        IPersistentShiftProfessorship persistentShiftProfessorship =
            ps.getIPersistentShiftProfessorship();
        IPersistentSupportLesson persistentSupportLesson = ps.getIPersistentSupportLesson();
        List destinationProfessorships = persistentProfessorship.readByExecutionCourse(destination);
        if (destinationProfessorships == null)
        {
            destinationProfessorships = new ArrayList();
        }
        List sourceProfessorships = persistentProfessorship.readByExecutionCourse(source);
        if (sourceProfessorships != null)
        {

            Iterator iter = sourceProfessorships.iterator();
            while (iter.hasNext())
            {
                IProfessorship professorship = (IProfessorship) iter.next();
                final IProfessorship professorship2Compare = professorship;
                if (CollectionUtils.find(destinationProfessorships, new Predicate()
                {

                    public boolean evaluate(Object arg0)
                    {
                        IProfessorship prof = (IProfessorship) arg0;
                            if (prof.getTeacher() == professorship2Compare.getTeacher())
                        {
                            return true; }
                        else
                        {
                            return false; }
                    }
                }) == null)
                {
                    persistentProfessorship.simpleLockWrite(professorship);
                    professorship.setExecutionCourse(destination);

                }
                else
                {
                    List associatedShiftsProfessorships =
                        professorship.getAssociatedShiftProfessorship();
                    Iterator iterator = associatedShiftsProfessorships.iterator();

                    while (iterator.hasNext())
                    {
                        IShiftProfessorship shiftProfessorship = (IShiftProfessorship) iterator.next();
                        persistentShiftProfessorship.delete(shiftProfessorship);
                    }

                    List supportLessons = persistentSupportLesson.readByProfessorship(professorship);
                    iterator = supportLessons.iterator();
                    while (iterator.hasNext())
                    {
                        ISupportLesson supportLesson = (ISupportLesson) iterator.next();
                        persistentSupportLesson.delete(supportLesson);
                    }

                    persistentProfessorship.delete(professorship);
                }
            }
        }
        IPersistentResponsibleFor persistentResponsibleFor = ps.getIPersistentResponsibleFor();
        List destinationResponsibleFor = persistentResponsibleFor.readByExecutionCourse(destination);
        if (destinationResponsibleFor == null)
        {
            destinationResponsibleFor = new ArrayList();
        }
        List sourceResponsibleFor = persistentResponsibleFor.readByExecutionCourse(source);
        if (sourceResponsibleFor != null)
        {
            Iterator iter = sourceResponsibleFor.iterator();
            while (iter.hasNext())
            {
                IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
                final IResponsibleFor responsibleFor2Compare = responsibleFor;
                if (CollectionUtils.find(destinationResponsibleFor, new Predicate()
                {

                    public boolean evaluate(Object arg0)
                    {
                        IResponsibleFor respons = (IResponsibleFor) arg0;
                            if (respons.getTeacher() == responsibleFor2Compare.getTeacher())
                        {
                            return true; }
                        else
                        {
                            return false; }
                    }
                }) == null)
                {
                    persistentResponsibleFor.simpleLockWrite(responsibleFor);
                    responsibleFor.setExecutionCourse(destination);

                }
                else
                {
                    persistentResponsibleFor.delete(responsibleFor);
                }
            }
        }

    }

}
