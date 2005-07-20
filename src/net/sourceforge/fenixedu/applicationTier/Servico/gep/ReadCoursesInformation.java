/*
 * Created on Dec 21, 2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationMethod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadCoursesInformation implements IService {

    public List run(Integer executionDegreeId, Boolean basic, String executionYearString)
            throws FenixServiceException, ExcepcaoPersistencia {
        
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();

            List professorships = null;
            IExecutionYear executionYear = null;
            if (executionYearString != null) {
                executionYear = persistentExecutionYear.readExecutionYearByName(executionYearString);
            } else {
                executionYear = persistentExecutionYear.readCurrentExecutionYear();
            }
            if (executionDegreeId == null) {
            List<IExecutionDegree> executionDegrees = persistentExecutionDegree
                    .readByExecutionYearAndDegreeType(executionYear.getYear(), DegreeType.DEGREE);
            List<Integer> degreeCurricularPlanIDs = getDegreeCurricularPlanIDs(executionDegrees);
            Integer executionYearID = (!degreeCurricularPlanIDs.isEmpty()) ? executionDegrees
                    .get(0).getExecutionYear().getIdInternal()
                    : null;
                if (basic == null) {
                professorships = persistentProfessorship.readByDegreeCurricularPlansAndExecutionYear(
                        degreeCurricularPlanIDs, executionYearID);
                } else {
                professorships = persistentProfessorship
                        .readByDegreeCurricularPlansAndExecutionYearAndBasic(degreeCurricularPlanIDs,
                                executionYearID, basic);
                }
            } else {
                IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                        ExecutionDegree.class, executionDegreeId);
                if (basic == null) {
                professorships = persistentProfessorship.readByDegreeCurricularPlanAndExecutionYear(
                        executionDegree.getDegreeCurricularPlan().getIdInternal(), executionDegree.getExecutionYear()
                                .getIdInternal());
                } else {
                professorships = persistentProfessorship.readByDegreeCurricularPlanAndBasic(
                        executionDegree.getDegreeCurricularPlan().getIdInternal(), executionDegree.getExecutionYear()
                                .getIdInternal(), basic);
                }
            }
            List executionCourses = (List) CollectionUtils.collect(professorships, new Transformer() {

                public Object transform(Object o) {
                    IProfessorship professorship = (IProfessorship) o;
                    return professorship.getExecutionCourse();
                }
            });
            executionCourses = removeDuplicates(executionCourses);
            List infoSiteCoursesInformation = new ArrayList();
            Iterator iter = executionCourses.iterator();
            while (iter.hasNext()) {
                IExecutionCourse executionCourse = (IExecutionCourse) iter.next();
                infoSiteCoursesInformation.add(getCourseInformation(executionCourse, sp, executionYear));
            }

            return infoSiteCoursesInformation;
    }

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

    public InfoSiteCourseInformation getCourseInformation(IExecutionCourse executionCourse,
            ISuportePersistente sp, IExecutionYear executionYear) throws ExcepcaoPersistencia {
        InfoSiteCourseInformation infoSiteCourseInformation = new InfoSiteCourseInformation();

        InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                .newInfoFromDomain(executionCourse);
        infoSiteCourseInformation.setInfoExecutionCourse(infoExecutionCourse);

        IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
        IEvaluationMethod evaluationMethod = persistentEvaluationMethod
                .readByIdExecutionCourse(executionCourse.getIdInternal());
        if (evaluationMethod == null) {
            InfoEvaluationMethod infoEvaluationMethod = new InfoEvaluationMethod();
            infoEvaluationMethod.setInfoExecutionCourse(infoExecutionCourse);
            infoSiteCourseInformation.setInfoEvaluationMethod(infoEvaluationMethod);
        } else {
            infoSiteCourseInformation.setInfoEvaluationMethod(InfoEvaluationMethod
                    .newInfoFromDomain(evaluationMethod));
        }

        List infoResponsibleTeachers = getInfoResponsibleTeachers(executionCourse, sp);
        infoSiteCourseInformation.setInfoResponsibleTeachers(infoResponsibleTeachers);

        List curricularCourses = executionCourse.getAssociatedCurricularCourses();
        List infoCurricularCourses = getInfoCurricularCourses(curricularCourses, sp, executionYear);
        infoSiteCourseInformation.setInfoCurricularCourses(infoCurricularCourses);

        List infoCurriculums = getInfoCurriculums(curricularCourses, sp, executionYear);
        infoSiteCourseInformation.setInfoCurriculums(infoCurriculums);

        List infoLecturingTeachers = getInfoLecturingTeachers(executionCourse, sp);
        infoSiteCourseInformation.setInfoLecturingTeachers(infoLecturingTeachers);

        List infoBibliographicReferences = getInfoBibliographicReferences(executionCourse, sp);
        infoSiteCourseInformation.setInfoBibliographicReferences(infoBibliographicReferences);

        List infoLessons = getInfoLessons(executionCourse, sp);
        infoSiteCourseInformation.setInfoLessons(getFilteredInfoLessons(infoLessons));
        infoSiteCourseInformation.setNumberOfTheoLessons(getNumberOfLessons(infoLessons,
                ShiftType.TEORICA.name(), sp));
        infoSiteCourseInformation.setNumberOfPratLessons(getNumberOfLessons(infoLessons,
                ShiftType.PRATICA.name(), sp));
        infoSiteCourseInformation.setNumberOfTheoPratLessons(getNumberOfLessons(infoLessons,
                ShiftType.TEORICO_PRATICA.name(), sp));
        infoSiteCourseInformation.setNumberOfLabLessons(getNumberOfLessons(infoLessons,
                ShiftType.LABORATORIAL.name() , sp));
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
        return infoSiteCourseInformation;
    }

    private Integer getNumberOfLessons(List infoLessons, String lessonType, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        final String lessonTypeForPredicate = lessonType;
        ITurnoPersistente persistentShift = sp.getITurnoPersistente();
        IAulaPersistente persistentLesson = sp.getIAulaPersistente();
        List lessonsOfType = (List) CollectionUtils.select(infoLessons, new Predicate() {

            public boolean evaluate(Object arg0) {
                if (((InfoLesson) arg0).getTipo().name() == lessonTypeForPredicate) {
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
                // List shifts;
                // IShift shift2;

                InfoLesson infoLesson = (InfoLesson) iter.next();
                ILesson lesson = (ILesson) persistentLesson.readByOID(Lesson.class, infoLesson
                        .getIdInternal());

                // shifts = persistentShift.readByLesson(lesson);
                shift = persistentShift.readByLesson(lesson.getIdInternal());

                // if (shifts != null && !shifts.isEmpty()) {
                if (shift != null) {

                    // IShift aux = (IShift) shifts.get(0);
                    // IShift aux = (IShift) shift2;
                    /*
                     * if (shift == null) { //shift = aux; shift = shift2; } if
                     * (shift == aux) {
                     */
                        temp.add(infoLesson);
                    // }
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
     */
    private List getFilteredInfoLessons(List infoLessons) {
        List filteredInfoLessons = new ArrayList();
        InfoLesson infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.TEORICA);
        if (infoLesson != null)
            filteredInfoLessons.add(infoLesson);
        infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.PRATICA);
        if (infoLesson != null)
            filteredInfoLessons.add(infoLesson);
        infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.LABORATORIAL);
        if (infoLesson != null)
            filteredInfoLessons.add(infoLesson);
        infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.TEORICO_PRATICA);
        if (infoLesson != null)
            filteredInfoLessons.add(infoLesson);
        return filteredInfoLessons;
    }

    private InfoLesson getFilteredInfoLessonByType(List infoLessons, ShiftType type) {
        final ShiftType lessonType = type;
        InfoLesson infoLesson = (InfoLesson) CollectionUtils.find(infoLessons, new Predicate() {

            public boolean evaluate(Object o) {
                InfoLesson infoLesson = (InfoLesson) o;
                return infoLesson.getTipo().equals(lessonType);
            }
        });
        return infoLesson;
    }

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

    private List getInfoLecturingTeachers(IExecutionCourse executionCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        List professorShips = persistentProfessorship.readByExecutionCourse(executionCourse.getIdInternal());
        List infoLecturingTeachers = new ArrayList();
        Iterator iter = professorShips.iterator();
        while (iter.hasNext()) {
            IProfessorship professorship = (IProfessorship) iter.next();
            ITeacher teacher = professorship.getTeacher();
            infoLecturingTeachers.add(InfoTeacherWithPerson.newInfoFromDomain(teacher));
        }
        return infoLecturingTeachers;
    }

    private List getInfoCurriculums(List curricularCourses, ISuportePersistente sp,
            IExecutionYear executionYear) throws ExcepcaoPersistencia {
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
                    .readCurriculumByCurricularCourseAndExecutionYear(curricularCourse.getIdInternal(),
                            executionYear.getEndDate());
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

    private List getInfoResponsibleTeachers(IExecutionCourse executionCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
  
        List responsiblesFor = executionCourse.responsibleFors();
        
        List infoResponsibleTeachers = new ArrayList();
        Iterator iter = responsiblesFor.iterator();
        while (iter.hasNext()) {
            IProfessorship responsibleFor = (IProfessorship) iter.next();
            ITeacher teacher = responsibleFor.getTeacher();
            infoResponsibleTeachers.add(InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher));
        }
        return infoResponsibleTeachers;
    }

    private List getInfoCurricularCourses(List curricularCourses, ISuportePersistente sp,
            IExecutionYear executionYear) throws ExcepcaoPersistencia {
        List infoCurricularCourses = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
            List curricularCourseScopes = sp.getIPersistentCurricularCourseScope()
                    .readCurricularCourseScopesByCurricularCourseInExecutionYear(curricularCourse.getIdInternal(),
                            executionYear.getBeginDate(),executionYear.getEndDate());
            List infoScopes = getInfoScopes(curricularCourseScopes, sp);

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                    .newInfoFromDomain(curricularCourse);
            infoCurricularCourse.setInfoScopes(infoScopes);
            infoCurricularCourses.add(infoCurricularCourse);
        }
        return infoCurricularCourses;
    }

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

    private List getInfoLessons(IExecutionCourse executionCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        ITurnoPersistente persistentShift = sp.getITurnoPersistente();

        List shifts = persistentShift.readByExecutionCourse(executionCourse.getIdInternal());

        List lessons = new ArrayList();

        for (int i = 0; i < shifts.size(); i++) {
            IShift shift = (IShift) shifts.get(i);
            lessons.addAll(shift.getAssociatedLessons());
        }

        List infoLessons = new ArrayList();
        Iterator iter = lessons.iterator();
        while (iter.hasNext()) {
            ILesson lesson = (ILesson) iter.next();

            infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
        }
        return infoLessons;
    }
    
    private List<Integer> getDegreeCurricularPlanIDs(final List<IExecutionDegree> executionDegrees) {
        final List<Integer> result = new ArrayList<Integer>();
        for (final IExecutionDegree executionDegree : executionDegrees) {
            result.add(executionDegree.getDegreeCurricularPlan().getIdInternal());
    }
        return result;
    }
}