/*
 * Created on Dec 21, 2003
 *  
 */
package ServidorAplicacao.Servico.gep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurriculum;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.gesdis.InfoCourseReport;
import DataBeans.gesdis.InfoSiteCourseInformation;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.IAula;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.ICursoExecucao;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import Dominio.gesdis.ICourseReport;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentCourseReport;
import Util.TipoAula;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCoursesInformation implements IService
{ /**
   *  
   */
    public ReadCoursesInformation()
    {
        super();
    }

    public List run(Integer executionDegreeId) throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();

            ICursoExecucao executionDegree =
                (ICursoExecucao) persistentExecutionDegree.readByOID(
                    CursoExecucao.class,
                    executionDegreeId);
            List professorships = persistentProfessorship.readByExecutionDegree(executionDegree);
            List executionCourses = (List) CollectionUtils.collect(professorships, new Transformer()
            {
                public Object transform(Object o)
                {
                    IProfessorship professorship = (IProfessorship) o;
                    return professorship.getExecutionCourse();
                }
            });
            executionCourses = removeDuplicates(executionCourses);
            List infoSiteCoursesInformation = new ArrayList();
            Iterator iter = executionCourses.iterator();
            while (iter.hasNext())
            {
                IExecutionCourse executionCourse = (IExecutionCourse) iter.next();
                infoSiteCoursesInformation.add(getCourseInformation(executionCourse, sp));
            }
            Collections.sort(infoSiteCoursesInformation, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    InfoSiteCourseInformation information1 = (InfoSiteCourseInformation) o1;
                    InfoSiteCourseInformation information2 = (InfoSiteCourseInformation) o2;
                    return information1.getInfoExecutionCourse().getNome().compareTo(
                        information2.getInfoExecutionCourse().getNome());
                }
            });
            return infoSiteCoursesInformation;

        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e.getMessage());
        }
    }

    /**
	 * @param executionCourses
	 * @return
	 */
    private List removeDuplicates(List executionCourses)
    {
        List result = new ArrayList();
        Iterator iter = executionCourses.iterator();
        while (iter.hasNext())
        {
            IExecutionCourse executionCourse = (IExecutionCourse) iter.next();
            if (!result.contains(executionCourse))
                result.add(executionCourse);
        }
        return result;
    }

    /**
	 * @param integer
	 * @return
	 */
    private InfoSiteCourseInformation getCourseInformation(IExecutionCourse executionCourse, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
        InfoSiteCourseInformation infoSiteCourseInformation = new InfoSiteCourseInformation();
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

        return infoSiteCourseInformation;
    }

    /**
	 * @param executionCourse
	 * @param sp
	 * @return @throws
	 *         ExcepcaoPersistencia
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

            List scopes = curricularCourse.getScopes();
            List infoScopes = getInfoScopes(scopes, sp);
            InfoCurricularCourse infoCurricularCourse =
                Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
            infoCurricularCourse.setInfoScopes(infoScopes);
            infoCurricularCourses.add(infoCurricularCourse);
        }
        return infoCurricularCourses;
    }

    /**
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
	 * @param executionCourse
	 * @param sp
	 * @return @throws
	 *         ExcepcaoPersistencia
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
            IAula lesson = (IAula) iter.next();
            infoLessons.add(Cloner.copyILesson2InfoLesson(lesson));
        }
        return infoLessons;
    }

    /**
	 * @param curricularCourses
	 * @param sp
	 * @return @throws
	 *         ExcepcaoPersistencia
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
            List infoScopes = getInfoScopes(curricularCourse.getScopes(), sp);
            infoCurricularCourse.setInfoScopes(infoScopes);
            ICurriculum curriculum =
                persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
            InfoCurriculum infoCurriculum = null;
            if (curriculum == null)
            {
                infoCurriculum = new InfoCurriculum();

            } else
            {
                infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
            }
            infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);
            infoCurriculums.add(infoCurriculum);
        }
        return infoCurriculums;
    }

    /**
	 * @param executionCourse
	 * @param sp
	 * @return @throws
	 *         ExcepcaoPersistencia
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
}