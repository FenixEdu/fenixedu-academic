/*
 * Created on Dec 21, 2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadCoursesInformation extends Service {

    public List run(Integer executionDegreeId, Boolean basic, String executionYearString)
            throws FenixServiceException, ExcepcaoPersistencia {

        List professorships = null;
        ExecutionYear executionYear = null;
        if (executionYearString != null) {
            executionYear = ExecutionYear.readExecutionYearByName(executionYearString);
        } else {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        }
        if (executionDegreeId == null) {
            List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByExecutionYearAndDegreeType(executionYear.getYear(), DegreeType.DEGREE);
            List<DegreeCurricularPlan> degreeCurricularPlans = getDegreeCurricularPlans(executionDegrees);
            ExecutionYear executionDegreesExecutionYear = (!degreeCurricularPlans.isEmpty()) ? executionDegrees.get(0)
                    .getExecutionYear() : null;
            if (basic == null) {
                professorships = Professorship.readByDegreeCurricularPlansAndExecutionYear(
                        degreeCurricularPlans, executionDegreesExecutionYear);
            } else {
                professorships = Professorship
                        .readByDegreeCurricularPlansAndExecutionYearAndBasic(degreeCurricularPlans,
                                executionDegreesExecutionYear, basic);
            }
        } else {
            ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
            if (basic == null) {
                professorships = Professorship.readByDegreeCurricularPlanAndExecutionYear(
                        executionDegree.getDegreeCurricularPlan(), executionDegree.getExecutionYear());
            } else {
                professorships = Professorship.readByDegreeCurricularPlanAndExecutionYearAndBasic(
                        executionDegree.getDegreeCurricularPlan(), executionDegree
                                .getExecutionYear(), basic);
            }
        }
        List executionCourses = (List) CollectionUtils.collect(professorships, new Transformer() {

            public Object transform(Object o) {
                Professorship professorship = (Professorship) o;
                return professorship.getExecutionCourse();
            }
        });
        executionCourses = removeDuplicates(executionCourses);
        List infoSiteCoursesInformation = new ArrayList();
        Iterator iter = executionCourses.iterator();
        while (iter.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) iter.next();
            infoSiteCoursesInformation.add(getCourseInformation(executionCourse, executionYear));
        }

        return infoSiteCoursesInformation;
    }

    private List removeDuplicates(List executionCourses) {
        List result = new ArrayList();
        Iterator iter = executionCourses.iterator();
        while (iter.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) iter.next();
            if (!result.contains(executionCourse))
                result.add(executionCourse);
        }
        return result;
    }

    public InfoSiteCourseInformation getCourseInformation(ExecutionCourse executionCourse,
            ExecutionYear executionYear) throws ExcepcaoPersistencia {
        InfoSiteCourseInformation infoSiteCourseInformation = new InfoSiteCourseInformation();

        InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                .newInfoFromDomain(executionCourse);
        infoSiteCourseInformation.setInfoExecutionCourse(infoExecutionCourse);

        final EvaluationMethod evaluationMethod = executionCourse.getEvaluationMethod();
        if (evaluationMethod == null) {
            InfoEvaluationMethod infoEvaluationMethod = new InfoEvaluationMethod();
            infoEvaluationMethod.setInfoExecutionCourse(infoExecutionCourse);
            infoSiteCourseInformation.setInfoEvaluationMethod(infoEvaluationMethod);
        } else {
            infoSiteCourseInformation.setInfoEvaluationMethod(InfoEvaluationMethod
                    .newInfoFromDomain(evaluationMethod));
        }

        List infoRepersistentSupportonsibleTeachers = getInfoRepersistentSupportonsibleTeachers(
                executionCourse);
        infoSiteCourseInformation.setInfoResponsibleTeachers(infoRepersistentSupportonsibleTeachers);

        List curricularCourses = executionCourse.getAssociatedCurricularCourses();
        List infoCurricularCourses = getInfoCurricularCourses(curricularCourses,
                executionYear);
        infoSiteCourseInformation.setInfoCurricularCourses(infoCurricularCourses);

        List infoCurriculums = getInfoCurriculums(curricularCourses, executionYear);
        infoSiteCourseInformation.setInfoCurriculums(infoCurriculums);

        List infoLecturingTeachers = getInfoLecturingTeachers(executionCourse);
        infoSiteCourseInformation.setInfoLecturingTeachers(infoLecturingTeachers);

        List infoBibliographicReferences = getInfoBibliographicReferences(executionCourse);
        infoSiteCourseInformation.setInfoBibliographicReferences(infoBibliographicReferences);

        List infoLessons = getInfoLessons(executionCourse);
        infoSiteCourseInformation.setInfoLessons(getFilteredInfoLessons(infoLessons));
        infoSiteCourseInformation.setNumberOfTheoLessons(getNumberOfLessons(infoLessons,
                ShiftType.TEORICA.name()));
        infoSiteCourseInformation.setNumberOfPratLessons(getNumberOfLessons(infoLessons,
                ShiftType.PRATICA.name()));
        infoSiteCourseInformation.setNumberOfTheoPratLessons(getNumberOfLessons(infoLessons,
                ShiftType.TEORICO_PRATICA.name()));
        infoSiteCourseInformation.setNumberOfLabLessons(getNumberOfLessons(infoLessons,
                ShiftType.LABORATORIAL.name()));
        CourseReport courseReport = executionCourse.getCourseReport();
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

    public static Integer getNumberOfLessons(List infoLessons, String lessonType) throws ExcepcaoPersistencia {
        final String lessonTypeForPredicate = lessonType;
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
            Shift shift = null;
            List temp = new ArrayList();
            while (iter.hasNext()) {
                // List shifts;
                // Shift shift2;

                InfoLesson infoLesson = (InfoLesson) iter.next();
                Lesson lesson = rootDomainObject.readLessonByOID(infoLesson
                        .getIdInternal());

                // shifts = persistentShift.readByLesson(lesson);
                shift = lesson.getShift();

                // if (shifts != null && !shifts.isEmpty()) {
                if (shift != null) {

                    // Shift aux = (Shift) shifts.get(0);
                    // Shift aux = (Shift) shift2;
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

    private List getInfoBibliographicReferences(ExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        List bibliographicReferences = executionCourse.getAssociatedBibliographicReferences();
        List infoBibliographicReferences = new ArrayList();
        Iterator iter = bibliographicReferences.iterator();
        while (iter.hasNext()) {
            BibliographicReference bibliographicReference = (BibliographicReference) iter.next();

            infoBibliographicReferences.add(InfoBibliographicReference
                    .newInfoFromDomain(bibliographicReference));
        }
        return infoBibliographicReferences;
    }

    private List getInfoLecturingTeachers(ExecutionCourse executionCourse) throws ExcepcaoPersistencia {

        List professorShips = executionCourse.getProfessorships();
        List infoLecturingTeachers = new ArrayList();
        Iterator iter = professorShips.iterator();
        while (iter.hasNext()) {
            Professorship professorship = (Professorship) iter.next();
            Teacher teacher = professorship.getTeacher();
            infoLecturingTeachers.add(InfoTeacher.newInfoFromDomain(teacher));
        }
        return infoLecturingTeachers;
    }

    private List getInfoCurriculums(List curricularCourses, 
            ExecutionYear executionYear) throws ExcepcaoPersistencia {
        List infoCurriculums = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iter.next();

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                    .newInfoFromDomain(curricularCourse);
            List infoScopes = getInfoScopes(curricularCourse.getScopesSet());
            infoCurricularCourse.setInfoScopes(infoScopes);
            Curriculum curriculum = curricularCourse.findLatestCurriculumModifiedBefore(executionYear.getEndDate());
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

    private List getInfoRepersistentSupportonsibleTeachers(ExecutionCourse executionCourse) throws ExcepcaoPersistencia {

        List repersistentSupportonsiblesFor = executionCourse.responsibleFors();

        List infoRepersistentSupportonsibleTeachers = new ArrayList();
        Iterator iter = repersistentSupportonsiblesFor.iterator();
        while (iter.hasNext()) {
            Professorship repersistentSupportonsibleFor = (Professorship) iter.next();
            Teacher teacher = repersistentSupportonsibleFor.getTeacher();
            infoRepersistentSupportonsibleTeachers.add(InfoTeacher
                    .newInfoFromDomain(teacher));
        }
        return infoRepersistentSupportonsibleTeachers;
    }

    private List getInfoCurricularCourses(List curricularCourses, ExecutionYear executionYear) throws ExcepcaoPersistencia {
        List infoCurricularCourses = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iter.next();
            Set<CurricularCourseScope> curricularCourseScopes = curricularCourse.findCurricularCourseScopesIntersectingPeriod(
                    executionYear.getBeginDate(), executionYear.getEndDate());
            List infoScopes = getInfoScopes(curricularCourseScopes);

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                    .newInfoFromDomain(curricularCourse);
            infoCurricularCourse.setInfoScopes(infoScopes);
            infoCurricularCourses.add(infoCurricularCourse);
        }
        return infoCurricularCourses;
    }

    private List getInfoScopes(Set<CurricularCourseScope> scopes) {
        List infoScopes = new ArrayList();
        Iterator iter = scopes.iterator();
        while (iter.hasNext()) {
            CurricularCourseScope curricularCourseScope = (CurricularCourseScope) iter.next();
            InfoCurricularCourseScope infoCurricularCourseScope = InfoCurricularCourseScope
                    .newInfoFromDomain(curricularCourseScope);
            infoScopes.add(infoCurricularCourseScope);
        }
        return infoScopes;
    }

    private List getInfoLessons(ExecutionCourse executionCourse) throws ExcepcaoPersistencia {

        List shifts = executionCourse.getAssociatedShifts();

        List lessons = new ArrayList();

        for (int i = 0; i < shifts.size(); i++) {
            Shift shift = (Shift) shifts.get(i);
            lessons.addAll(shift.getAssociatedLessons());
        }

        List infoLessons = new ArrayList();
        Iterator iter = lessons.iterator();
        while (iter.hasNext()) {
            Lesson lesson = (Lesson) iter.next();

            infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
        }
        return infoLessons;
    }

    private List<DegreeCurricularPlan> getDegreeCurricularPlans(final List<ExecutionDegree> executionDegrees) {
        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            result.add(executionDegree.getDegreeCurricularPlan());
        }
        return result;
    }
}