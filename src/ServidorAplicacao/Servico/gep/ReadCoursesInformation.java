/*
 * Created on Dec 21, 2003
 */
package ServidorAplicacao.Servico.gep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoBibliographicReference;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import DataBeans.InfoCurricularCourseWithInfoDegree;
import DataBeans.InfoCurriculum;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseWithExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoTeacherWithPerson;
import DataBeans.InfoTeacherWithPersonAndCategory;
import DataBeans.gesdis.InfoCourseReport;
import DataBeans.gesdis.InfoSiteCourseInformation;
import Dominio.Aula;
import Dominio.CursoExecucao;
import Dominio.IAula;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.ICursoExecucao;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionCourse;
import Dominio.IExecutionYear;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.ITurno;
import Dominio.ResponsibleFor;
import Dominio.gesdis.ICourseReport;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentCourseReport;
import Util.TipoAula;
import Util.TipoCurso;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadCoursesInformation implements IService {

    /**
     *  
     */
    public ReadCoursesInformation() {
        super();
    }

    public List run(Integer executionDegreeId, Boolean basic)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente persistentExecutionDegree = sp
                    .getICursoExecucaoPersistente();
            IPersistentProfessorship persistentProfessorship = sp
                    .getIPersistentProfessorship();
            List professorships = null;
            if (executionDegreeId == null) {
                IPersistentExecutionYear persistentExecutionYear = sp
                        .getIPersistentExecutionYear();
                IExecutionYear executionYear = persistentExecutionYear
                        .readCurrentExecutionYear();
                
                //FIXME
                // this piece of code is getting only degrees in case the choice made for search was
                // null execution degree and null basic. when search starts to be made for master degrees also
                // this code should be replaced bythe code bellow
                List executionDegrees = null;
                if (basic == null) {
                    executionDegrees = persistentExecutionDegree.readByExecutionYearAndDegreeType(
                            executionYear, TipoCurso.LICENCIATURA_OBJ);
                    professorships = persistentProfessorship
                            .readByExecutionDegrees(executionDegrees);
                } else {
                    executionDegrees = persistentExecutionDegree
                    .readByExecutionYear(executionYear.getYear());
                    professorships = persistentProfessorship
                            .readByExecutionDegreesAndBasic(executionDegrees,
                                    basic);
                }
//                List executionDegrees = persistentExecutionDegree
//                .readByExecutionYear(executionYear.getYear());
//
//                if (basic == null) {
//                    professorships = persistentProfessorship
//                            .readByExecutionDegrees(executionDegrees);
//                } else {
//                    professorships = persistentProfessorship
//                            .readByExecutionDegreesAndBasic(executionDegrees,
//                                    basic);
//                }
            } else {
                ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree
                        .readByOID(CursoExecucao.class, executionDegreeId);
                if (basic == null) {
                    professorships = persistentProfessorship
                            .readByExecutionDegree(executionDegree);
                } else {
                    professorships = persistentProfessorship
                            .readByExecutionDegreeAndBasic(executionDegree,
                                    basic);
                }
            }
            List executionCourses = (List) CollectionUtils.collect(
                    professorships, new Transformer() {

                        public Object transform(Object o) {
                            IProfessorship professorship = (IProfessorship) o;
                            return professorship.getExecutionCourse();
                        }
                    });
            executionCourses = removeDuplicates(executionCourses);
            List infoSiteCoursesInformation = new ArrayList();
            Iterator iter = executionCourses.iterator();
            while (iter.hasNext()) {
                IExecutionCourse executionCourse = (IExecutionCourse) iter
                        .next();
                infoSiteCoursesInformation.add(getCourseInformation(
                        executionCourse, sp));
            }

            return infoSiteCoursesInformation;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

    /**
     * @param executionCourses
     * @return
     */
    private List removeDuplicates(List executionCourses) {
        List result = new ArrayList();
        Iterator iter = executionCourses.iterator();
        while (iter.hasNext()) {
            IExecutionCourse executionCourse = (IExecutionCourse) iter.next();
            if (!result.contains(executionCourse))
                result.add(executionCourse);
        }
        return result;
    }

    public InfoSiteCourseInformation getCourseInformation(
            IExecutionCourse executionCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        InfoSiteCourseInformation infoSiteCourseInformation = new InfoSiteCourseInformation();

        //CLONER
        //InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse)
        // Cloner.get(executionCourse);
        InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                .newInfoFromDomain(executionCourse);
        infoSiteCourseInformation.setInfoExecutionCourse(infoExecutionCourse);

        IPersistentEvaluationMethod persistentEvaluationMethod = sp
                .getIPersistentEvaluationMethod();
        IEvaluationMethod evaluationMethod = persistentEvaluationMethod
                .readByExecutionCourse(executionCourse);
        if (evaluationMethod == null) {
            InfoEvaluationMethod infoEvaluationMethod = new InfoEvaluationMethod();
            infoEvaluationMethod.setInfoExecutionCourse(infoExecutionCourse);
            infoSiteCourseInformation
                    .setInfoEvaluationMethod(infoEvaluationMethod);
        } else {
            //CLONER
            //infoSiteCourseInformation.setInfoEvaluationMethod(Cloner
            //.copyIEvaluationMethod2InfoEvaluationMethod(evaluationMethod));
            infoSiteCourseInformation
                    .setInfoEvaluationMethod(InfoEvaluationMethod
                            .newInfoFromDomain(evaluationMethod));
        }

        List infoResponsibleTeachers = getInfoResponsibleTeachers(
                executionCourse, sp);
        infoSiteCourseInformation
                .setInfoResponsibleTeachers(infoResponsibleTeachers);

        List curricularCourses = executionCourse
                .getAssociatedCurricularCourses();
        List infoCurricularCourses = getInfoCurricularCourses(
                curricularCourses, sp);
        infoSiteCourseInformation
                .setInfoCurricularCourses(infoCurricularCourses);

        List infoCurriculums = getInfoCurriculums(curricularCourses, sp);
        infoSiteCourseInformation.setInfoCurriculums(infoCurriculums);

        List infoLecturingTeachers = getInfoLecturingTeachers(executionCourse,
                sp);
        infoSiteCourseInformation
                .setInfoLecturingTeachers(infoLecturingTeachers);

        List infoBibliographicReferences = getInfoBibliographicReferences(
                executionCourse, sp);
        infoSiteCourseInformation
                .setInfoBibliographicReferences(infoBibliographicReferences);

        List infoLessons = getInfoLessons(executionCourse, sp);
        infoSiteCourseInformation
                .setInfoLessons(getFilteredInfoLessons(infoLessons));
        infoSiteCourseInformation.setNumberOfTheoLessons(getNumberOfLessons(
                infoLessons, TipoAula.TEORICA, sp));
        infoSiteCourseInformation.setNumberOfPratLessons(getNumberOfLessons(
                infoLessons, TipoAula.PRATICA, sp));
        infoSiteCourseInformation
                .setNumberOfTheoPratLessons(getNumberOfLessons(infoLessons,
                        TipoAula.TEORICO_PRATICA, sp));
        infoSiteCourseInformation.setNumberOfLabLessons(getNumberOfLessons(
                infoLessons, TipoAula.LABORATORIAL, sp));
        IPersistentCourseReport persistentCourseReport = sp
                .getIPersistentCourseReport();
        ICourseReport courseReport = persistentCourseReport
                .readCourseReportByExecutionCourse(executionCourse);
        if (courseReport == null) {
            InfoCourseReport infoCourseReport = new InfoCourseReport();
            infoCourseReport.setInfoExecutionCourse(infoExecutionCourse);
            infoSiteCourseInformation.setInfoCourseReport(infoCourseReport);
        } else {
            //CLONER
            //infoSiteCourseInformation.setInfoCourseReport(Cloner
            //.copyICourseReport2InfoCourseReport(courseReport));
            infoSiteCourseInformation.setInfoCourseReport(InfoCourseReport
                    .newInfoFromDomain(courseReport));
        }
        return infoSiteCourseInformation;
    }

    /**
     * @param infoLessons
     * @param i
     * @return
     */
    private Integer getNumberOfLessons(List infoLessons, int lessonType,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        final int lessonTypeForPredicate = lessonType;
        ITurnoPersistente persistentShift = sp.getITurnoPersistente();
        IAulaPersistente persistentLesson = sp.getIAulaPersistente();
        List lessonsOfType = (List) CollectionUtils.select(infoLessons,
                new Predicate() {

                    public boolean evaluate(Object arg0) {
                        if (((InfoLesson) arg0).getTipo().getTipo().intValue() == lessonTypeForPredicate) {
                            return true;
                        }
                        return false;
                    }
                });
        if (lessonsOfType != null && !lessonsOfType.isEmpty()) {
            Iterator iter = lessonsOfType.iterator();
            ITurno shift = null;
            List temp = new ArrayList();
            while (iter.hasNext()) {
                List shifts;
                InfoLesson infoLesson = (InfoLesson) iter.next();
                IAula lesson = (IAula) persistentLesson.readByOID(Aula.class,
                        infoLesson.getIdInternal());
                shifts = persistentShift.readByLesson(lesson);
                if (shifts != null && !shifts.isEmpty()) {
                    ITurno aux = (ITurno) shifts.get(0);
                    if (shift == null) {
                        shift = aux;
                    }
                    if (shift == aux) {
                        temp.add(infoLesson);
                    }
                }
            }
            return new Integer(temp.size());
        }
        return null;

    }

    /**
     * Filter all the lessons to remove duplicates entries of lessons with the
     * same type
     * 
     * @param infoLessons
     * @return
     */
    private List getFilteredInfoLessons(List infoLessons) {
        List filteredInfoLessons = new ArrayList();
        InfoLesson infoLesson = getFilteredInfoLessonByType(infoLessons,
                new TipoAula(TipoAula.TEORICA));
        if (infoLesson != null)
            filteredInfoLessons.add(infoLesson);
        infoLesson = getFilteredInfoLessonByType(infoLessons, new TipoAula(
                TipoAula.PRATICA));
        if (infoLesson != null)
            filteredInfoLessons.add(infoLesson);
        infoLesson = getFilteredInfoLessonByType(infoLessons, new TipoAula(
                TipoAula.LABORATORIAL));
        if (infoLesson != null)
            filteredInfoLessons.add(infoLesson);
        infoLesson = getFilteredInfoLessonByType(infoLessons, new TipoAula(
                TipoAula.TEORICO_PRATICA));
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
    private InfoLesson getFilteredInfoLessonByType(List infoLessons,
            TipoAula type) {
        final TipoAula lessonType = type;
        InfoLesson infoLesson = (InfoLesson) CollectionUtils.find(infoLessons,
                new Predicate() {

                    public boolean evaluate(Object o) {
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
            IExecutionCourse executionCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentBibliographicReference persistentBibliographicReference = sp
                .getIPersistentBibliographicReference();
        List bibliographicReferences = persistentBibliographicReference
                .readBibliographicReference(executionCourse);
        List infoBibliographicReferences = new ArrayList();
        Iterator iter = bibliographicReferences.iterator();
        while (iter.hasNext()) {
            IBibliographicReference bibliographicReference = (IBibliographicReference) iter
                    .next();
            //CLONER
            //infoBibliographicReferences.add(Cloner
            //.copyIBibliographicReference2InfoBibliographicReference(bibliographicReference));
            infoBibliographicReferences.add(InfoBibliographicReference
                    .newInfoFromDomain(bibliographicReference));
        }
        return infoBibliographicReferences;
    }

    /**
     * @param executionCourse
     * @param sp
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private List getInfoLecturingTeachers(IExecutionCourse executionCourse,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentProfessorship persistentProfessorship = sp
                .getIPersistentProfessorship();
        List professorShips = persistentProfessorship
                .readByExecutionCourse(executionCourse);
        List infoLecturingTeachers = new ArrayList();
        Iterator iter = professorShips.iterator();
        while (iter.hasNext()) {
            IProfessorship professorship = (IProfessorship) iter.next();
            ITeacher teacher = professorship.getTeacher();
            //CLONER
            //infoLecturingTeachers.add(Cloner.copyITeacher2InfoTeacher(teacher));
            infoLecturingTeachers.add(InfoTeacherWithPerson
                    .newInfoFromDomain(teacher));
        }
        return infoLecturingTeachers;
    }

    /**
     * @param curricularCourses
     * @param sp
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private List getInfoCurriculums(List curricularCourses,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentCurriculum persistentCurriculum = sp
                .getIPersistentCurriculum();
        List infoCurriculums = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter
                    .next();
            //CLONER
            //InfoCurricularCourse infoCurricularCourse = Cloner
            //.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                    .newInfoFromDomain(curricularCourse);
            List infoScopes = getInfoScopes(curricularCourse.getScopes(), sp);
            infoCurricularCourse.setInfoScopes(infoScopes);
            ICurriculum curriculum = persistentCurriculum
                    .readCurriculumByCurricularCourse(curricularCourse);
            InfoCurriculum infoCurriculum = null;
            if (curriculum == null) {
                infoCurriculum = new InfoCurriculum();
            } else {
                //CLONER
                //infoCurriculum =
                // Cloner.copyICurriculum2InfoCurriculum(curriculum);
                infoCurriculum = InfoCurriculum.newInfoFromDomain(curriculum);
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
    private List getInfoResponsibleTeachers(IExecutionCourse executionCourse,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentResponsibleFor persistentResponsibleFor = sp
                .getIPersistentResponsibleFor();
        List responsiblesFor = persistentResponsibleFor
                .readByExecutionCourse(executionCourse);
        List infoResponsibleTeachers = new ArrayList();
        Iterator iter = responsiblesFor.iterator();
        while (iter.hasNext()) {
            ResponsibleFor responsibleFor = (ResponsibleFor) iter.next();
            ITeacher teacher = responsibleFor.getTeacher();
            //CLONER
            //infoResponsibleTeachers.add(Cloner.copyITeacher2InfoTeacher(teacher));
            infoResponsibleTeachers.add(InfoTeacherWithPersonAndCategory
                    .newInfoFromDomain(teacher));
        }
        return infoResponsibleTeachers;
    }

    /**
     * @param curricularCourses
     * @param sp
     * @return
     */
    private List getInfoCurricularCourses(List curricularCourses,
            ISuportePersistente sp) {
        List infoCurricularCourses = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter
                    .next();
            //FIXME
            //this test is to be taken off when search of courses starts to be made also for master degrees
            if(curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(TipoCurso.LICENCIATURA_OBJ)) {
	            List curricularCourseScopes = curricularCourse.getScopes();
	            List infoScopes = getInfoScopes(curricularCourseScopes, sp);
	            //CLONER
	            //InfoCurricularCourse infoCurricularCourse = Cloner
	            //.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
	            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
	                    .newInfoFromDomain(curricularCourse);
	            infoCurricularCourse.setInfoScopes(infoScopes);
	            infoCurricularCourses.add(infoCurricularCourse);
            }
        }
        return infoCurricularCourses;
    }

    /**
     * @param scopes
     * @param sp
     * @return
     */
    private List getInfoScopes(List scopes, ISuportePersistente sp) {
        List infoScopes = new ArrayList();
        Iterator iter = scopes.iterator();
        while (iter.hasNext()) {
            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iter
                    .next();
            //CLONER
            //InfoCurricularCourseScope infoCurricularCourseScope = Cloner
            //.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
            InfoCurricularCourseScope infoCurricularCourseScope = InfoCurricularCourseScopeWithBranchAndSemesterAndYear
                    .newInfoFromDomain(curricularCourseScope);
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
    private List getInfoLessons(IExecutionCourse executionCourse,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IAulaPersistente persistentLesson = sp.getIAulaPersistente();
        List lessons = persistentLesson.readByExecutionCourse(executionCourse);
        List infoLessons = new ArrayList();
        Iterator iter = lessons.iterator();
        while (iter.hasNext()) {
            IAula lesson = (IAula) iter.next();
            //CLONER
            //infoLessons.add(Cloner.copyILesson2InfoLesson(lesson));
            infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
        }
        return infoLessons;
    }
}