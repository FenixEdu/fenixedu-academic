/*
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.gep.ReadCoursesInformation;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationInformation;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationStatistics;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCourseInformation extends Service {

    public TeacherAdministrationSiteView run(Integer executionCourseId) throws FenixServiceException,
            ExcepcaoPersistencia {
        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView();

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

        InfoSiteCourseInformation infoSiteCourseInformation = new InfoSiteCourseInformation();

        InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                .newInfoFromDomain(executionCourse);
        infoExecutionCourse.setNumberOfAttendingStudents(executionCourse.getAttendsCount());

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

        List repersistentSupportonsiblesFor = executionCourse.responsibleFors();

        List infoRepersistentSupportonsibleTeachers = getInfoRepersistentSupportonsibleTeachers(repersistentSupportonsiblesFor);
        infoSiteCourseInformation.setInfoResponsibleTeachers(infoRepersistentSupportonsibleTeachers);

        List curricularCourses = executionCourse.getAssociatedCurricularCourses();
        List infoCurricularCourses = getInfoCurricularCourses(curricularCourses);
        infoSiteCourseInformation.setInfoCurricularCourses(infoCurricularCourses);

        List infoCurriculums = getInfoCurriculums(curricularCourses);
        infoSiteCourseInformation.setInfoCurriculums(infoCurriculums);

        List infoLecturingTeachers = getInfoLecturingTeachers(executionCourse);
        infoSiteCourseInformation.setInfoLecturingTeachers(infoLecturingTeachers);

        List infoBibliographicReferences = getInfoBibliographicReferences(executionCourse);
        infoSiteCourseInformation.setInfoBibliographicReferences(infoBibliographicReferences);

        List infoDepartments = getInfoDepartments(repersistentSupportonsiblesFor);
        infoSiteCourseInformation.setInfoDepartments(infoDepartments);

        List infoLessons = getInfoLessons(executionCourse);
        infoSiteCourseInformation.setInfoLessons(getFilteredInfoLessons(infoLessons));
        infoSiteCourseInformation.setNumberOfTheoLessons(ReadCoursesInformation.getNumberOfLessons(infoLessons,
                ShiftType.TEORICA.name()));
        infoSiteCourseInformation.setNumberOfPratLessons(ReadCoursesInformation.getNumberOfLessons(infoLessons,
                ShiftType.PRATICA.name()));
        infoSiteCourseInformation.setNumberOfTheoPratLessons(ReadCoursesInformation.getNumberOfLessons(infoLessons,
                ShiftType.TEORICO_PRATICA.name()));
        infoSiteCourseInformation.setNumberOfLabLessons(ReadCoursesInformation.getNumberOfLessons(infoLessons,
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

        List infoSiteEvaluationInformations = getInfoSiteEvaluationInformation(executionCourse
                .getExecutionPeriod(), curricularCourses);
        infoSiteCourseInformation.setInfoSiteEvaluationInformations(infoSiteEvaluationInformations);

        Site site = executionCourse.getSite();

        siteView.setComponent(infoSiteCourseInformation);
        ISiteComponent commonComponent = new InfoSiteCommon();
        TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
        siteView.setCommonComponent(commonComponent);

        return siteView;
    }

    private List removeDuplicates(List repersistentSupportonsiblesFor) {
        List<Professorship> result = new ArrayList<Professorship>();
        Iterator iter = repersistentSupportonsiblesFor.iterator();
        while (iter.hasNext()) {
            Professorship repersistentSupportonsibleFor = (Professorship) iter.next();
            if (!result.contains(repersistentSupportonsibleFor))
                result.add(repersistentSupportonsibleFor);
        }
        return result;
    }

    /**
     * @param period
     * @param curricularCourses
     * @param persistentSupport
     * @return
     */
    private List getInfoSiteEvaluationInformation(ExecutionPeriod executionPeriod,
            List curricularCourses) throws ExcepcaoPersistencia {
        List infoSiteEvalutationInformations = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iter.next();

            InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
            List<Enrolment> enrolled = curricularCourse.getEnrolmentsByExecutionPeriod(executionPeriod);
            infoSiteEvaluationStatistics.setEnrolled(new Integer(enrolled.size()));
            infoSiteEvaluationStatistics.setEvaluated(getEvaluated(enrolled));
            infoSiteEvaluationStatistics.setApproved(getApproved(enrolled));

            InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod
                    .newInfoFromDomain(executionPeriod);
            infoSiteEvaluationStatistics.setInfoExecutionPeriod(infoExecutionPeriod);

            InfoSiteEvaluationInformation infoSiteEvaluationInformation = new InfoSiteEvaluationInformation();
            infoSiteEvaluationInformation.setInfoSiteEvaluationStatistics(infoSiteEvaluationStatistics);

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                    .newInfoFromDomain(curricularCourse);
            infoSiteEvaluationInformation.setInfoCurricularCourse(infoCurricularCourse);
            infoSiteEvaluationInformation.setInfoSiteEvaluationHistory(getInfoSiteEvaluationsHistory(
                    executionPeriod, curricularCourse));
            infoSiteEvalutationInformations.add(infoSiteEvaluationInformation);
        }
        return infoSiteEvalutationInformations;
    }

    /**
     * @param executionPeriod
     * @param curricularCourse
     * @param persistentSupport
     * @return
     */
    private List getInfoSiteEvaluationsHistory(final ExecutionPeriod executionPeriodToTest,
            CurricularCourse curricularCourse) throws ExcepcaoPersistencia {

        List infoSiteEvaluationsHistory = new ArrayList();

        Set<ExecutionPeriod> executionPeriods = new HashSet<ExecutionPeriod>();

        for (Iterator executionCourseIter = curricularCourse.getAssociatedExecutionCourses().iterator(); executionCourseIter
                .hasNext();) {
            ExecutionCourse executionCourse = (ExecutionCourse) executionCourseIter.next();
            ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();

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
            ExecutionPeriod executionPeriod = (ExecutionPeriod) iter.next();

            InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();

            infoSiteEvaluationStatistics.setInfoExecutionPeriod(InfoExecutionPeriod
                    .newInfoFromDomain(executionPeriod));
            List<Enrolment> enrolled = curricularCourse.getEnrolmentsByExecutionPeriod(executionPeriod);
            infoSiteEvaluationStatistics.setEnrolled(new Integer(enrolled.size()));
            infoSiteEvaluationStatistics.setEvaluated(getEvaluated(enrolled));
            infoSiteEvaluationStatistics.setApproved(getApproved(enrolled));
            infoSiteEvaluationsHistory.add(infoSiteEvaluationStatistics);
        }

        Collections.sort(infoSiteEvaluationsHistory, new ReverseComparator(new BeanComparator("infoExecutionPeriod.infoExecutionYear.year")));
        
        return infoSiteEvaluationsHistory;
    }

    /**
     * @param curricularCourses
     * @param persistentSupport
     * @return
     */
    private Integer getApproved(List enrolments) {
        int approved = 0;
        Iterator iter = enrolments.iterator();
        while (iter.hasNext()) {
            Enrolment enrolment = (Enrolment) iter.next();
            EnrollmentState enrollmentState = enrolment.getEnrollmentState();
            if (enrollmentState.equals(EnrollmentState.APROVED)) {
                approved++;
            }
        }
        return new Integer(approved);
    }

    /**
     * @param curricularCourses
     * @param persistentSupport
     * @return
     */
    private Integer getEvaluated(List enrolments) {
        int evaluated = 0;
        Iterator iter = enrolments.iterator();
        while (iter.hasNext()) {
            Enrolment enrolment = (Enrolment) iter.next();
            EnrollmentState enrollmentState = enrolment.getEnrollmentState();
            if (enrollmentState.equals(EnrollmentState.APROVED)
                    || enrollmentState.equals(EnrollmentState.NOT_APROVED)) {
                evaluated++;
            }
        }
        return new Integer(evaluated);
    }


    /**
     * @param executionCourse
     * @param persistentSupport
     * @return
     */
    private List getInfoDepartments(List repersistentSupportonsiblesFor)
            throws ExcepcaoPersistencia {
        
        List infoDepartments = new ArrayList();
        repersistentSupportonsiblesFor = removeDuplicates(repersistentSupportonsiblesFor);
        Iterator iter = repersistentSupportonsiblesFor.iterator();
        while (iter.hasNext()) {
            Professorship repersistentSupportonsibleFor = (Professorship) iter.next();
            Teacher teacher = repersistentSupportonsibleFor.getTeacher();
            Department department = teacher.getCurrentWorkingDepartment();

            infoDepartments.add(InfoDepartment.newInfoFromDomain(department));
        }
        return infoDepartments;
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

    /**
     * Filter the lessons to select the first element of the given type
     * 
     * @param infoLessons
     * @return
     */
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

    /**
     * @param executionCourse
     * @param persistentSupport
     * @return
     */
    private List getInfoBibliographicReferences(ExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
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

    private List getInfoLecturingTeachers(ExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        
        List professorShips = executionCourse.getProfessorships();

        List infoLecturingTeachers = new ArrayList();
        Iterator iter = professorShips.iterator();
        while (iter.hasNext()) {
            Professorship professorship = (Professorship) iter.next();
            Teacher teacher = professorship.getTeacher();

            infoLecturingTeachers.add(InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher));
        }
        return infoLecturingTeachers;
    }

    private List getInfoCurriculums(List curricularCourses)
            throws ExcepcaoPersistencia {

        List infoCurriculums = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iter.next();

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                    .newInfoFromDomain(curricularCourse);
            List infoScopes = getInfoScopes(curricularCourse.getScopes());
            infoCurricularCourse.setInfoScopes(infoScopes);
            Curriculum curriculum = curricularCourse.findLatestCurriculum();
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
     * @param persistentSupport
     * @return
     * @throws ExcepcaoPersistencia
     */
    private List getInfoRepersistentSupportonsibleTeachers(List repersistentSupportonsiblesFor) {
        List infoRepersistentSupportonsibleTeachers = new ArrayList();
        Iterator iter = repersistentSupportonsiblesFor.iterator();
        while (iter.hasNext()) {
            Professorship repersistentSupportonsibleFor = (Professorship) iter.next();
            Teacher teacher = repersistentSupportonsibleFor.getTeacher();

            infoRepersistentSupportonsibleTeachers.add(InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher));
        }
        return infoRepersistentSupportonsibleTeachers;
    }

    /**
     * @param curricularCourses
     * @param persistentSupport
     * @return
     */
    private List getInfoCurricularCourses(List curricularCourses) {
        List infoCurricularCourses = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iter.next();

            List curricularCourseScopes = curricularCourse.getScopes();
            List infoScopes = getInfoScopes(curricularCourseScopes);
            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                    .newInfoFromDomain(curricularCourse);
            infoCurricularCourse.setInfoScopes(infoScopes);
            infoCurricularCourses.add(infoCurricularCourse);
        }
        return infoCurricularCourses;
    }

    private List getInfoScopes(List scopes) {
        List infoScopes = new ArrayList();
        Iterator iter = scopes.iterator();
        while (iter.hasNext()) {
            CurricularCourseScope curricularCourseScope = (CurricularCourseScope) iter.next();

            InfoCurricularCourseScope infoCurricularCourseScope = InfoCurricularCourseScopeWithBranchAndSemesterAndYear
                    .newInfoFromDomain(curricularCourseScope);
            infoScopes.add(infoCurricularCourseScope);
        }
        return infoScopes;
    }

    private List<InfoLesson> getInfoLessons(ExecutionCourse executionCourse) {
    	final List<InfoLesson> result = new ArrayList<InfoLesson>();
    	for (final Shift shift : executionCourse.getAssociatedShiftsSet()) {
    		for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
    			result.add(InfoLesson.newInfoFromDomain(lesson));
    		}
    	}
    	return result;
    }
}