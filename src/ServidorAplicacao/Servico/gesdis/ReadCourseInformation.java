/*
 * Created on 12/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ISiteComponent;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurriculum;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoSiteCommon;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.gesdis.InfoCourseReport;
import DataBeans.gesdis.InfoSiteCourseInformation;
import DataBeans.gesdis.InfoSiteEvaluationInformation;
import DataBeans.gesdis.InfoSiteEvaluationStatistics;
import DataBeans.util.Cloner;
import Dominio.Aula;
import Dominio.ExecutionCourse;
import Dominio.IAula;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.IDepartment;
import Dominio.IEnrolment;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISite;
import Dominio.ITeacher;
import Dominio.ITurno;
import Dominio.ResponsibleFor;
import Dominio.gesdis.ICourseReport;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentCourseReport;
import Util.EnrolmentState;
import Util.TipoAula;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCourseInformation implements IService
{
    /**
	 *  
	 */
    public ReadCourseInformation()
    {}

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

            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
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

            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            List responsiblesFor = persistentResponsibleFor.readByExecutionCourse(executionCourse);

            List infoResponsibleTeachers = getInfoResponsibleTeachers(responsiblesFor, sp);
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

            List infoDepartments = getInfoDepartments(responsiblesFor, sp);
            infoSiteCourseInformation.setInfoDepartments(infoDepartments);

            List infoLessons = getInfoLessons(executionCourse, sp);
            infoSiteCourseInformation.setInfoLessons(getFilteredInfoLessons(infoLessons));
            infoSiteCourseInformation.setNumberOfTheoLessons(
                getNumberOfLessons(infoLessons, TipoAula.TEORICA, sp));
            infoSiteCourseInformation.setNumberOfPratLessons(
                getNumberOfLessons(infoLessons, TipoAula.PRATICA, sp));
            infoSiteCourseInformation.setNumberOfTheoPratLessons(
                getNumberOfLessons(infoLessons, TipoAula.TEORICO_PRATICA, sp));
            infoSiteCourseInformation.setNumberOfLabLessons(
                getNumberOfLessons(infoLessons, TipoAula.LABORATORIAL, sp));
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

            List infoSiteEvaluationInformations =
                getInfoSiteEvaluationInformation(
                    executionCourse.getExecutionPeriod(),
                    curricularCourses,
                    sp);
            infoSiteCourseInformation.setInfoSiteEvaluationInformations(infoSiteEvaluationInformations);

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

    private List removeDuplicates(List responsiblesFor)
    {
        List result = new ArrayList();
        Iterator iter = responsiblesFor.iterator();
        while (iter.hasNext())
        {
            IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
            if (!result.contains(responsibleFor))
                result.add(responsibleFor);
        }
        return result;
    }

    /**
	 * @param period
	 * @param curricularCourses
	 * @param sp
	 * @return
	 */
    private List getInfoSiteEvaluationInformation(
        IExecutionPeriod executionPeriod,
        List curricularCourses,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        List infoSiteEvalutationInformations = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();

            InfoSiteEvaluationStatistics infoSiteEvaluationStatistics =
                new InfoSiteEvaluationStatistics();
            List enrolled = getEnrolled(executionPeriod, curricularCourse, sp);
            infoSiteEvaluationStatistics.setEnrolled(new Integer(enrolled.size()));
            infoSiteEvaluationStatistics.setEvaluated(getEvaluated(enrolled));
            infoSiteEvaluationStatistics.setApproved(getApproved(enrolled));
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);
            infoSiteEvaluationStatistics.setInfoExecutionPeriod(infoExecutionPeriod);

            InfoSiteEvaluationInformation infoSiteEvaluationInformation =
                new InfoSiteEvaluationInformation();
            infoSiteEvaluationInformation.setInfoSiteEvaluationStatistics(infoSiteEvaluationStatistics);
            InfoCurricularCourse infoCurricularCourse =
                Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
            infoSiteEvaluationInformation.setInfoCurricularCourse(infoCurricularCourse);
            infoSiteEvaluationInformation.setInfoSiteEvaluationHistory(
                getInfoSiteEvaluationsHistory(executionPeriod.getSemester(), curricularCourse, sp));
            infoSiteEvalutationInformations.add(infoSiteEvaluationInformation);
        }
        return infoSiteEvalutationInformations;
    }

    /**
	 * @param executionPeriod
	 * @param curricularCourse
	 * @param sp
	 * @return
	 */
    private List getInfoSiteEvaluationsHistory(
        Integer semester,
        ICurricularCourse curricularCourse,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        List infoSiteEvaluationsHistory = new ArrayList();
        List executionPeriods =
            (
                List) CollectionUtils
                    .collect(curricularCourse.getAssociatedExecutionCourses(), new Transformer()
        {
            public Object transform(Object arg0)
            {
                IExecutionCourse executionCourse = (IExecutionCourse) arg0;
                return executionCourse.getExecutionPeriod();
            }

        });
        // filter the executionPeriods by semester
        final Integer historySemester = semester;
        executionPeriods = (List) CollectionUtils.select(executionPeriods, new Predicate()
        {
            public boolean evaluate(Object arg0)
            {
                IExecutionPeriod executionPeriod = (IExecutionPeriod) arg0;
                return executionPeriod.getSemester().equals(historySemester);
            }
        });
        Collections.sort(executionPeriods, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                IExecutionPeriod executionPeriod1 = (IExecutionPeriod) o1;
                IExecutionPeriod executionPeriod2 = (IExecutionPeriod) o2;
                return executionPeriod1.getExecutionYear().getYear().compareTo(
                    executionPeriod2.getExecutionYear().getYear());
            }
        });
        Iterator iter = executionPeriods.iterator();
        while (iter.hasNext())
        {
            IExecutionPeriod executionPeriod = (IExecutionPeriod) iter.next();

            InfoSiteEvaluationStatistics infoSiteEvaluationStatistics =
                new InfoSiteEvaluationStatistics();
            infoSiteEvaluationStatistics.setInfoExecutionPeriod(
                (InfoExecutionPeriod) Cloner.get(executionPeriod));
            List enrolled = getEnrolled(executionPeriod, curricularCourse, sp);
            infoSiteEvaluationStatistics.setEnrolled(new Integer(enrolled.size()));
            infoSiteEvaluationStatistics.setEvaluated(getEvaluated(enrolled));
            infoSiteEvaluationStatistics.setApproved(getApproved(enrolled));
            infoSiteEvaluationsHistory.add(infoSiteEvaluationStatistics);
        }

        return infoSiteEvaluationsHistory;
    }

    /**
	 * @param curricularCourses
	 * @param sp
	 * @return
	 */
    private Integer getApproved(List enrolments) throws ExcepcaoPersistencia
    {
        int approved = 0;
        Iterator iter = enrolments.iterator();
        while (iter.hasNext())
        {
            IEnrolment enrolment = (IEnrolment) iter.next();
            EnrolmentState enrolmentState = enrolment.getEnrolmentState();
            if (enrolmentState.equals(EnrolmentState.APROVED))
            {
                approved++;
            }
        }
        return new Integer(approved);
    }

    /**
	 * @param curricularCourses
	 * @param sp
	 * @return
	 */
    private Integer getEvaluated(List enrolments) throws ExcepcaoPersistencia
    {
        int evaluated = 0;
        Iterator iter = enrolments.iterator();
        while (iter.hasNext())
        {
            IEnrolment enrolment = (IEnrolment) iter.next();
            EnrolmentState enrolmentState = enrolment.getEnrolmentState();
            if (enrolmentState.equals(EnrolmentState.APROVED)
                || enrolmentState.equals(EnrolmentState.NOT_APROVED)
                || enrolmentState.equals(EnrolmentState.NOT_EVALUATED))
            {
                evaluated++;
            }
        }
        return new Integer(evaluated);
    }

    /**
	 * @param curricularCourses
	 * @param sp
	 * @return
	 */
    private List getEnrolled(
        IExecutionPeriod executionPeriod,
        ICurricularCourse curricularCourse,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
        List enrolments =
            persistentEnrolment.readByCurricularCourseAndExecutionPeriod(
                curricularCourse,
                executionPeriod);
        return enrolments;
    }

    /**
	 * @param executionCourse
	 * @param sp
	 * @return
	 */
    private List getInfoDepartments(List responsiblesFor, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentDepartment persistentDepartment = sp.getIDepartamentoPersistente();
        List infoDepartments = new ArrayList();
        responsiblesFor = removeDuplicates(responsiblesFor);
        Iterator iter = responsiblesFor.iterator();
        while (iter.hasNext())
        {
            ResponsibleFor responsibleFor = (ResponsibleFor) iter.next();
            ITeacher teacher = responsibleFor.getTeacher();
            IDepartment department = persistentDepartment.readByTeacher(teacher);
            infoDepartments.add(Cloner.copyIDepartment2InfoDepartment(department));
        }
        return infoDepartments;
    }

    /**
	 * @param infoLessons
	 * @param i
	 * @return
	 */
    private Integer getNumberOfLessons(List infoLessons, int lessonType, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {

        final int lessonTypeForPredicate = lessonType;
        ITurnoPersistente persistentShift = sp.getITurnoPersistente();
        IAulaPersistente persistentLesson = sp.getIAulaPersistente();
        List lessonsOfType = (List) CollectionUtils.select(infoLessons, new Predicate()
        {

            public boolean evaluate(Object arg0)
            {
                if (((InfoLesson) arg0).getTipo().getTipo().intValue() == lessonTypeForPredicate)
                {
                    return true;
                }
                return false;
            }
        });
        if (lessonsOfType != null && !lessonsOfType.isEmpty())
        {

            Iterator iter = lessonsOfType.iterator();
            ITurno shift = null;

            List temp = new ArrayList();
            while (iter.hasNext())
            {
                List shifts;

                InfoLesson infoLesson = (InfoLesson) iter.next();
                IAula lesson =
                    (IAula) persistentLesson.readByOId(new Aula(infoLesson.getIdInternal()), false);

                shifts = persistentShift.readByLesson(lesson);

                if (shifts != null && !shifts.isEmpty())
                {

                    ITurno aux = (ITurno) shifts.get(0);
                    if (shift == null)
                    {
                        shift = aux;
                    }
                    if (shift == aux)
                    {
                        temp.add(infoLesson);
                    }
                }

            }
            return new Integer(temp.size());
        } else
        {

            return null;
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
    private List getInfoResponsibleTeachers(List responsiblesFor, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
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
}