/*
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationInformation;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationStatistics;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.ResponsibleFor;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationMethod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;
import net.sourceforge.fenixedu.util.TipoAula;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCourseInformation implements IService {

    public TeacherAdministrationSiteView run(Integer executionCourseId) throws FenixServiceException,
            ExcepcaoPersistencia {
        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseId);

        InfoSiteCourseInformation infoSiteCourseInformation = new InfoSiteCourseInformation();

        InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                .newInfoFromDomain(executionCourse);
        infoExecutionCourse.setNumberOfAttendingStudents(new Integer(executionCourse
                .getAttendingStudents() == null ? 0 : executionCourse.getAttendingStudents().size()));

        infoSiteCourseInformation.setInfoExecutionCourse(infoExecutionCourse);

        IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
        IEvaluationMethod evaluationMethod = persistentEvaluationMethod
                .readByExecutionCourse(executionCourse);
        if (evaluationMethod == null) {
            InfoEvaluationMethod infoEvaluationMethod = new InfoEvaluationMethod();
            infoEvaluationMethod.setInfoExecutionCourse(infoExecutionCourse);
            infoSiteCourseInformation.setInfoEvaluationMethod(infoEvaluationMethod);

        } else {

            infoSiteCourseInformation.setInfoEvaluationMethod(InfoEvaluationMethod
                    .newInfoFromDomain(evaluationMethod));
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
        infoSiteCourseInformation.setNumberOfTheoLessons(getNumberOfLessons(infoLessons,
                TipoAula.TEORICA, sp));
        infoSiteCourseInformation.setNumberOfPratLessons(getNumberOfLessons(infoLessons,
                TipoAula.PRATICA, sp));
        infoSiteCourseInformation.setNumberOfTheoPratLessons(getNumberOfLessons(infoLessons,
                TipoAula.TEORICO_PRATICA, sp));
        infoSiteCourseInformation.setNumberOfLabLessons(getNumberOfLessons(infoLessons,
                TipoAula.LABORATORIAL, sp));
        IPersistentCourseReport persistentCourseReport = sp.getIPersistentCourseReport();
        ICourseReport courseReport = persistentCourseReport
                .readCourseReportByExecutionCourse(executionCourse.getIdInternal());

        if (courseReport == null) {
            InfoCourseReport infoCourseReport = new InfoCourseReport();
            infoCourseReport.setInfoExecutionCourse(infoExecutionCourse);
            infoSiteCourseInformation.setInfoCourseReport(infoCourseReport);
        } else {
            infoSiteCourseInformation.setInfoCourseReport(InfoCourseReport
                    .newInfoFromDomain(courseReport));
        }

        List infoSiteEvaluationInformations = getInfoSiteEvaluationInformation(executionCourse
                .getExecutionPeriod(), curricularCourses, sp);
        infoSiteCourseInformation.setInfoSiteEvaluationInformations(infoSiteEvaluationInformations);

        IPersistentSite persistentSite = sp.getIPersistentSite();
        ISite site = persistentSite.readByExecutionCourse(executionCourse);

        siteView.setComponent(infoSiteCourseInformation);
        ISiteComponent commonComponent = new InfoSiteCommon();
        TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
        siteView.setCommonComponent(commonComponent);

        return siteView;
    }

    private List removeDuplicates(List responsiblesFor) {
        List<IResponsibleFor> result = new ArrayList<IResponsibleFor>();
        Iterator iter = responsiblesFor.iterator();
        while (iter.hasNext()) {
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
    private List getInfoSiteEvaluationInformation(IExecutionPeriod executionPeriod,
            List curricularCourses, ISuportePersistente sp) throws ExcepcaoPersistencia {
        List infoSiteEvalutationInformations = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();

            InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
            List enrolled = getEnrolled(executionPeriod, curricularCourse, sp);
            infoSiteEvaluationStatistics.setEnrolled(new Integer(enrolled.size()));
            infoSiteEvaluationStatistics.setEvaluated(getEvaluated(enrolled));
            infoSiteEvaluationStatistics.setApproved(getApproved(enrolled));

            InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear
                    .newInfoFromDomain(executionPeriod);
            infoSiteEvaluationStatistics.setInfoExecutionPeriod(infoExecutionPeriod);

            InfoSiteEvaluationInformation infoSiteEvaluationInformation = new InfoSiteEvaluationInformation();
            infoSiteEvaluationInformation.setInfoSiteEvaluationStatistics(infoSiteEvaluationStatistics);

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                    .newInfoFromDomain(curricularCourse);
            infoSiteEvaluationInformation.setInfoCurricularCourse(infoCurricularCourse);
            infoSiteEvaluationInformation.setInfoSiteEvaluationHistory(getInfoSiteEvaluationsHistory(
                    executionPeriod, curricularCourse, sp));
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
    private List getInfoSiteEvaluationsHistory(final IExecutionPeriod executionPeriodToTest,
            ICurricularCourse curricularCourse, ISuportePersistente sp) throws ExcepcaoPersistencia {

        List infoSiteEvaluationsHistory = new ArrayList();

        SortedSet<IExecutionPeriod> executionPeriods = new TreeSet(new Comparator<IExecutionPeriod>() {
            public int compare(IExecutionPeriod executionPeriod1, IExecutionPeriod executionPeriod2) {
                return executionPeriod1.getExecutionYear().getYear().compareTo(
                        executionPeriod2.getExecutionYear().getYear());
            }
        });

        for (Iterator executionCourseIter = curricularCourse.getAssociatedExecutionCourses().iterator(); executionCourseIter
                .hasNext();) {
            IExecutionCourse executionCourse = (IExecutionCourse) executionCourseIter.next();
            IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();

            // filter the executionPeriods by semester;
            // also, information regarding execution years after the course's
            // execution year must not be shown
            if (executionPeriod.getSemester().equals(executionPeriodToTest.getSemester()))
                if (executionPeriod.getExecutionYear().getBeginDate().before(
                        executionPeriodToTest.getExecutionYear().getBeginDate()))
                    executionPeriods.add(executionPeriod);
        }

        Iterator iter = executionPeriods.iterator();
        while (iter.hasNext()) {
            IExecutionPeriod executionPeriod = (IExecutionPeriod) iter.next();

            InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();

            infoSiteEvaluationStatistics.setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear
                    .newInfoFromDomain(executionPeriod));
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
    private Integer getApproved(List enrolments) {
        int approved = 0;
        Iterator iter = enrolments.iterator();
        while (iter.hasNext()) {
            IEnrolment enrolment = (IEnrolment) iter.next();
            EnrollmentState enrollmentState = enrolment.getEnrollmentState();
            if (enrollmentState.equals(EnrollmentState.APROVED)) {
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
    private Integer getEvaluated(List enrolments) {
        int evaluated = 0;
        Iterator iter = enrolments.iterator();
        while (iter.hasNext()) {
            IEnrolment enrolment = (IEnrolment) iter.next();
            EnrollmentState enrollmentState = enrolment.getEnrollmentState();
            if (enrollmentState.equals(EnrollmentState.APROVED)
                    || enrollmentState.equals(EnrollmentState.NOT_APROVED)) {
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
    private List getEnrolled(IExecutionPeriod executionPeriod, ICurricularCourse curricularCourse,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();
        List enrolments = persistentEnrolment.readByCurricularCourseAndExecutionPeriod(curricularCourse,
                executionPeriod);
        return enrolments;
    }

    /**
     * @param executionCourse
     * @param sp
     * @return
     */
    private List getInfoDepartments(List responsiblesFor, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentDepartment persistentDepartment = sp.getIDepartamentoPersistente();
        List infoDepartments = new ArrayList();
        responsiblesFor = removeDuplicates(responsiblesFor);
        Iterator iter = responsiblesFor.iterator();
        while (iter.hasNext()) {
            ResponsibleFor responsibleFor = (ResponsibleFor) iter.next();
            ITeacher teacher = responsibleFor.getTeacher();
            IDepartment department = persistentDepartment.readByTeacher(teacher);

            infoDepartments.add(InfoDepartment.newInfoFromDomain(department));
        }
        return infoDepartments;
    }

    /**
     * @param infoLessons
     * @param i
     * @return
     */
    private Integer getNumberOfLessons(List infoLessons, int lessonType, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        final int lessonTypeForPredicate = lessonType;
        ITurnoPersistente persistentShift = sp.getITurnoPersistente();
        IAulaPersistente persistentLesson = sp.getIAulaPersistente();
        List lessonsOfType = (List) CollectionUtils.select(infoLessons, new Predicate() {

            public boolean evaluate(Object arg0) {
                if (((InfoLesson) arg0).getTipo().getTipo().intValue() == lessonTypeForPredicate) {
                    return true;
                }
                return false;
            }
        });
        if (lessonsOfType != null && !lessonsOfType.isEmpty()) {

            Iterator iter = lessonsOfType.iterator();
            IShift shift = null;

            List temp = new ArrayList();
            while (iter.hasNext()) {
                List shifts;

                InfoLesson infoLesson = (InfoLesson) iter.next();
                ILesson lesson = (ILesson) persistentLesson.readByOID(Lesson.class, infoLesson
                        .getIdInternal());

                shifts = persistentShift.readByLesson(lesson);

                if (shifts != null && !shifts.isEmpty()) {

                    IShift aux = (IShift) shifts.get(0);
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
    private InfoLesson getFilteredInfoLessonByType(List infoLessons, TipoAula type) {
        final TipoAula lessonType = type;
        InfoLesson infoLesson = (InfoLesson) CollectionUtils.find(infoLessons, new Predicate() {
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
    private List getInfoBibliographicReferences(IExecutionCourse executionCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentBibliographicReference persistentBibliographicReference = sp
                .getIPersistentBibliographicReference();
        List bibliographicReferences = persistentBibliographicReference
                .readBibliographicReference(executionCourse.getIdInternal());

        List infoBibliographicReferences = new ArrayList();
        Iterator iter = bibliographicReferences.iterator();
        while (iter.hasNext()) {
            IBibliographicReference bibliographicReference = (IBibliographicReference) iter.next();

            infoBibliographicReferences.add(InfoBibliographicReference
                    .newInfoFromDomain(bibliographicReference));
        }
        return infoBibliographicReferences;
    }

    /**
     * @param executionCourse
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    private List getInfoLecturingTeachers(IExecutionCourse executionCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        List professorShips = persistentProfessorship.readByExecutionCourse(executionCourse);

        List infoLecturingTeachers = new ArrayList();
        Iterator iter = professorShips.iterator();
        while (iter.hasNext()) {
            IProfessorship professorship = (IProfessorship) iter.next();
            ITeacher teacher = professorship.getTeacher();

            infoLecturingTeachers.add(InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher));
        }
        return infoLecturingTeachers;
    }

    /**
     * @param curricularCourses
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    private List getInfoCurriculums(List curricularCourses, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

        List infoCurriculums = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                    .newInfoFromDomain(curricularCourse);
            List infoScopes = getInfoScopes(curricularCourse.getScopes(), sp);
            infoCurricularCourse.setInfoScopes(infoScopes);
            ICurriculum curriculum = persistentCurriculum
                    .readCurriculumByCurricularCourse(curricularCourse.getIdInternal());
            InfoCurriculum infoCurriculum = null;
            if (curriculum == null) {
                infoCurriculum = new InfoCurriculum();

            } else {
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
     * @return
     * @throws ExcepcaoPersistencia
     */
    private List getInfoResponsibleTeachers(List responsiblesFor, ISuportePersistente sp) {
        List infoResponsibleTeachers = new ArrayList();
        Iterator iter = responsiblesFor.iterator();
        while (iter.hasNext()) {
            ResponsibleFor responsibleFor = (ResponsibleFor) iter.next();
            ITeacher teacher = responsibleFor.getTeacher();

            infoResponsibleTeachers.add(InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher));
        }
        return infoResponsibleTeachers;
    }

    /**
     * @param curricularCourses
     * @param sp
     * @return
     */
    private List getInfoCurricularCourses(List curricularCourses, ISuportePersistente sp) {
        List infoCurricularCourses = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();

            List curricularCourseScopes = curricularCourse.getScopes();
            List infoScopes = getInfoScopes(curricularCourseScopes, sp);
            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                    .newInfoFromDomain(curricularCourse);
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
    private List getInfoScopes(List scopes, ISuportePersistente sp) {
        List infoScopes = new ArrayList();
        Iterator iter = scopes.iterator();
        while (iter.hasNext()) {
            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iter.next();

            InfoCurricularCourseScope infoCurricularCourseScope = InfoCurricularCourseScopeWithBranchAndSemesterAndYear
                    .newInfoFromDomain(curricularCourseScope);
            infoScopes.add(infoCurricularCourseScope);
        }
        return infoScopes;
    }

    /**
     * @param executionCourse
     * @param sp
     * @return
     * @throws ExcepcaoPersistencia
     */
    private List getInfoLessons(IExecutionCourse executionCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        List lessons = new ArrayList();

        List shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
        for (int i = 0; i < shifts.size(); i++) {
            IShift shift = (IShift) shifts.get(i);
            List aulasTemp = shift.getAssociatedLessons();

            lessons.addAll(aulasTemp);
        }

        List infoLessons = new ArrayList();
        Iterator iter = lessons.iterator();
        while (iter.hasNext()) {
            ILesson lesson = (ILesson) iter.next();

            InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);
            IShift shift = lesson.getShift();
            InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            infoLesson.setInfoShift(infoShift);

            infoLessons.add(infoLesson);
        }
        return infoLessons;
    }
}