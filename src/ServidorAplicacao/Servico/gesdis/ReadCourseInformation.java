/*
 * Created on 12/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.ISiteComponent;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurriculum;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoSiteCommon;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.gesdis.InfoCourseReport;
import DataBeans.gesdis.InfoSiteCourseInformation;
import DataBeans.util.Cloner;
import Dominio.Aula;
import Dominio.ExecutionCourse;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ISite;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import Dominio.gesdis.ICourseReport;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentCourseReport;
import Util.TipoAula;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCourseInformation implements IServico
{

    private static ReadCourseInformation service = new ReadCourseInformation();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadCourseInformation getService()
    {
        return service;
    }

    /**
	 *  
	 */
    private ReadCourseInformation()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public final String getNome()
    {
        return "ReadCourseInformation";
    }

    /**
	 * Executes the service.
	 */
    public TeacherAdministrationSiteView run(Integer executionCourseId) throws FenixServiceException
    {
        try
        {
            TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView();

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            IExecutionCourse executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(
                    new ExecutionCourse(executionCourseId),
                    false);

            InfoSiteCourseInformation infoSiteCourseInformation = new InfoSiteCourseInformation();

            InfoExecutionCourse infoExecutionCourse =
                 Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
            infoSiteCourseInformation.setInfoExecutionCourse(infoExecutionCourse);

            IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
            IEvaluationMethod evaluationMethod =
                persistentEvaluationMethod.readByExecutionCourse(executionCourse);
            if (evaluationMethod == null)
            {
                InfoEvaluationMethod infoEvaluationMethod = new InfoEvaluationMethod();
                infoEvaluationMethod.setInfoExecutionCourse(infoExecutionCourse);
                infoSiteCourseInformation.setInfoEvaluationMethod(infoEvaluationMethod);

            } else
            {
                infoSiteCourseInformation.setInfoEvaluationMethod(
                    Cloner.copyIEvaluationMethod2InfoEvaluationMethod(evaluationMethod));
            }

            List infoResponsibleTeachers = getInfoResponsibleTeachers(executionCourse, sp);
            infoSiteCourseInformation.setInfoResponsibleTeachers(infoResponsibleTeachers);

            List curricularCourses = executionCourse.getAssociatedCurricularCourses();
            List infoCurricularCourses = getInfoCurricularCourses(curricularCourses, sp);
            infoSiteCourseInformation.setInfoCurricularCourses(infoCurricularCourses);

            List infoCurriculums = getInfoCurriculums(curricularCourses, sp);
            infoSiteCourseInformation.setInfoCurriculums(infoCurriculums);

            List infoLecturingTeachers = getInfoLecturingTeachers(executionCourse, sp);
            infoSiteCourseInformation.setInfoLecturingTeachers(infoLecturingTeachers);

            List infoBibliographicReferences = getInfoBibliographicReferences(executionCourse, sp);
            infoSiteCourseInformation.setInfoBibliographicReferences(infoBibliographicReferences);

            List infoLessons = getInfoLessons(executionCourse, sp);
            infoSiteCourseInformation.setInfoLessons(getFilteredInfoLessons(infoLessons));

            IPersistentCourseReport persistentCourseReport = sp.getIPersistentCourseReport();
            ICourseReport courseReport =
                persistentCourseReport.readCourseReportByExecutionCourse(executionCourse);

            if (courseReport == null)
            {
                InfoCourseReport infoCourseReport = new InfoCourseReport();
                infoCourseReport.setInfoExecutionCourse(infoExecutionCourse);
                infoSiteCourseInformation.setInfoCourseReport(infoCourseReport);
            } else
            {
                infoSiteCourseInformation.setInfoCourseReport(
                    Cloner.copyICourseReport2InfoCourseReport(courseReport));
            }

            IPersistentSite persistentSite = sp.getIPersistentSite();
            ISite site = persistentSite.readByExecutionCourse(executionCourse);

            siteView.setComponent(infoSiteCourseInformation);
            ISiteComponent commonComponent = new InfoSiteCommon();
            TeacherAdministrationSiteComponentBuilder componentBuilder =
                TeacherAdministrationSiteComponentBuilder.getInstance();
            commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
            siteView.setCommonComponent(commonComponent);

            return siteView;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }

    /**
	 * Filter all the lessons to remove duplicates entries of lessons with the same type
	 * 
	 * @param infoLessons
	 * @return
	 */
    private List getFilteredInfoLessons(List infoLessons)
    {
        List filteredInfoLessons = new ArrayList();
        InfoLesson infoLesson = getFilteredInfoLessonByType(infoLessons, new TipoAula(TipoAula.TEORICA));
        if (infoLesson != null)
            filteredInfoLessons.add(infoLesson);

        infoLesson = getFilteredInfoLessonByType(infoLessons, new TipoAula(TipoAula.PRATICA));
        if (infoLesson != null)
            filteredInfoLessons.add(infoLesson);

        infoLesson = getFilteredInfoLessonByType(infoLessons, new TipoAula(TipoAula.LABORATORIAL));
        if (infoLesson != null)
            filteredInfoLessons.add(infoLesson);

        infoLesson = getFilteredInfoLessonByType(infoLessons, new TipoAula(TipoAula.TEORICO_PRATICA));
        if (infoLesson != null)
            filteredInfoLessons.add(infoLesson);
        return filteredInfoLessons;
    }

    /**
	 * Filter the lessons to select the first element of the given type
	 * 
	 * @param infoLessons
	 * @return
	 */
    private InfoLesson getFilteredInfoLessonByType(List infoLessons, TipoAula type)
    {
        final TipoAula lessonType = type;
        InfoLesson infoLesson = (InfoLesson) CollectionUtils.find(infoLessons, new Predicate()
        {
            public boolean evaluate(Object o)
            {
                InfoLesson infoLesson = (InfoLesson) o;
                return infoLesson.getTipo().equals(lessonType);
            }
        });
        return infoLesson;
    }

    /**
	 * @param executionCourse
	 * @param sp
	 * @return
	 */
    private List getInfoBibliographicReferences(
        IExecutionCourse executionCourse,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentBibliographicReference persistentBibliographicReference =
            sp.getIPersistentBibliographicReference();
        List bibliographicReferences =
            persistentBibliographicReference.readBibliographicReference(executionCourse);

        List infoBibliographicReferences = new ArrayList();
        Iterator iter = bibliographicReferences.iterator();
        while (iter.hasNext())
        {
            IBibliographicReference bibliographicReference = (IBibliographicReference) iter.next();
            infoBibliographicReferences.add(
                Cloner.copyIBibliographicReference2InfoBibliographicReference(bibliographicReference));
        }
        return infoBibliographicReferences;
    }

    /**
     * 
     * @param executionCourse
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    private List getInfoLecturingTeachers(IExecutionCourse executionCourse, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        List professorShips = persistentProfessorship.readByExecutionCourse(executionCourse);

        List infoLecturingTeachers = new ArrayList();
        Iterator iter = professorShips.iterator();
        while (iter.hasNext())
        {
            IProfessorship professorship = (IProfessorship) iter.next();
            ITeacher teacher = professorship.getTeacher();
            infoLecturingTeachers.add(Cloner.copyITeacher2InfoTeacher(teacher));
        }
        return infoLecturingTeachers;
    }

    /**
     * 
     * @param curricularCourses
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    private List getInfoCurriculums(List curricularCourses, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

        List infoCurriculums = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
            InfoCurricularCourse infoCurricularCourse =
                Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
            ICurriculum curriculum =
                persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
            if (curriculum == null)
            {
                InfoCurriculum infoCurriculum = new InfoCurriculum();
                infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);
                infoCurriculums.add(infoCurriculum);
            } else
            {
                infoCurriculums.add(Cloner.copyICurriculum2InfoCurriculum(curriculum));
            }

        }
        return infoCurriculums;
    }

    /**
     * 
     * @param executionCourse
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    private List getInfoResponsibleTeachers(IExecutionCourse executionCourse, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
        List responsiblesFor = persistentResponsibleFor.readByExecutionCourse(executionCourse);

        List infoResponsibleTeachers = new ArrayList();
        Iterator iter = responsiblesFor.iterator();
        while (iter.hasNext())
        {
            ResponsibleFor responsibleFor = (ResponsibleFor) iter.next();
            ITeacher teacher = responsibleFor.getTeacher();
            infoResponsibleTeachers.add(Cloner.copyITeacher2InfoTeacher(teacher));
        }
        return infoResponsibleTeachers;
    }

    /**
     * 
     * @param curricularCourses
     * @param sp
     * @return
     */
    private List getInfoCurricularCourses(List curricularCourses, ISuportePersistente sp)
    {
        List infoCurricularCourses = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();

            List curricularCourseScopes = curricularCourse.getScopes();
            List infoScopes = getInfoScopes(curricularCourseScopes, sp);
            InfoCurricularCourse infoCurricularCourse =
                Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
            infoCurricularCourse.setInfoScopes(infoScopes);
            infoCurricularCourses.add(infoCurricularCourse);
        }
        return infoCurricularCourses;
    }

    /**
     * 
     * @param scopes
     * @param sp
     * @return
     */
    private List getInfoScopes(List scopes, ISuportePersistente sp)
    {
        List infoScopes = new ArrayList();
        Iterator iter = scopes.iterator();
        while (iter.hasNext())
        {
            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iter.next();
            InfoCurricularCourseScope infoCurricularCourseScope =
                Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
            infoScopes.add(infoCurricularCourseScope);
        }
        return infoScopes;
    }

    /**
     * 
     * @param executionCourse
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    private List getInfoLessons(IExecutionCourse executionCourse, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IAulaPersistente persistentLesson = sp.getIAulaPersistente();
        List lessons = persistentLesson.readByExecutionCourse(executionCourse);

        List infoLessons = new ArrayList();
        Iterator iter = lessons.iterator();
        while (iter.hasNext())
        {
            Aula lesson = (Aula) iter.next();
            infoLessons.add(Cloner.copyILesson2InfoLesson(lesson));
        }
        return infoLessons;
    }
}
