/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 12/Nov/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.gesdis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class InfoSiteCourseInformation extends DataTranferObject implements ISiteComponent {

    private final ExecutionCourse executionCourseDomainReference;

    private ExecutionYear executionYearDomainReference;

    public InfoSiteCourseInformation(final ExecutionCourse executionCourse) {
        executionCourseDomainReference = executionCourse;
    }

    public InfoSiteCourseInformation(final ExecutionCourse executionCourse, final ExecutionYear executionYear) {
        this(executionCourse);
        executionYearDomainReference = executionYear;
    }

    public ExecutionCourse getExecutionCourse() {
        return this.executionCourseDomainReference;
    }

    private ExecutionYear getExecutionYear() {
        return this.executionYearDomainReference;
    }

    // =================== FIELDS RETRIEVED BY DOMAIN LOGIC
    // =======================

    public InfoExecutionCourse getInfoExecutionCourse() {
        return InfoExecutionCourse.newInfoFromDomain(getExecutionCourse());
    }

    public InfoCourseReport getInfoCourseReport() {
        return getInfoExecutionCourse().getInfoCourseReport();
    }

    public List<InfoCurricularCourse> getInfoCurricularCourses() {
        return getExecutionYear() == null ? getInfoExecutionCourse().getAssociatedInfoCurricularCourses() : getInfoExecutionCourse()
                .getAssociatedInfoCurricularCourses(getExecutionYear());
    }

    public List<InfoTeacher> getInfoLecturingTeachers() {
        final List<InfoTeacher> result = new ArrayList<InfoTeacher>();

        for (final Professorship professorship : getExecutionCourse().getProfessorships()) {
            result.add(InfoTeacher.newInfoFromDomain(professorship.getPerson()));
        }

        return result;
    }

    public List<InfoTeacher> getInfoResponsibleTeachers() {
        final List<InfoTeacher> result = new ArrayList<InfoTeacher>();

        for (final Professorship responsibleFor : getExecutionCourse().responsibleFors()) {
            result.add(InfoTeacher.newInfoFromDomain(responsibleFor.getPerson()));
        }

        return result;
    }

    // =================== FIELDS NOT RETRIEVED BY DOMAIN LOGIC
    // =======================

    public Integer getNumberOfTheoLessons() {
        return getNumberOfLessons(getExecutionCourse().getShiftsByTypeOrderedByShiftName(ShiftType.TEORICA));
    }

    public Integer getNumberOfPratLessons() {
        return getNumberOfLessons(getExecutionCourse().getShiftsByTypeOrderedByShiftName(ShiftType.PRATICA));
    }

    public Integer getNumberOfTheoPratLessons() {
        return getNumberOfLessons(getExecutionCourse().getShiftsByTypeOrderedByShiftName(ShiftType.TEORICO_PRATICA));
    }

    public Integer getNumberOfLabLessons() {
        return getNumberOfLessons(getExecutionCourse().getShiftsByTypeOrderedByShiftName(ShiftType.LABORATORIAL));
    }

    private Integer getNumberOfLessons(final Collection<Shift> shifts) {
        int result = 0;
        for (final Shift shift : shifts) {
            result += shift.getAssociatedLessons().size();
        }
        return result;
    }

    static final private int MIN_LENGTH = 10;

    public Integer getNumberOfFieldsFilled() {
        int result = 0;

        if (!getInfoLecturingTeachers().isEmpty()) {
            result++;
        }

        Iterator iter = getExecutionCourse().getAssociatedBibliographicReferences().iterator();
        while (iter.hasNext()) {
            BibliographicReference bibliographicReference = (BibliographicReference) iter.next();
            if (bibliographicReference.getTitle() != null && bibliographicReference.getTitle().length() > MIN_LENGTH) {
                result++;
                break;
            }
        }
        if (getExecutionCourse().getEvaluationMethod() != null
                && getExecutionCourse().getEvaluationMethod().getEvaluationElements() != null
                && getExecutionCourse().getEvaluationMethod().getEvaluationElements().getContent(MultiLanguageString.pt) != null
                && getExecutionCourse().getEvaluationMethod().getEvaluationElements().getContent(MultiLanguageString.pt).length() > MIN_LENGTH) {
            result++;
        }

        iter = getInfoCurriculums().iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getGeneralObjectives() != null && infoCurriculum.getGeneralObjectives().length() > MIN_LENGTH) {
                result++;
                break;
            }
        }

        iter = getInfoCurriculums().iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getProgram() != null && infoCurriculum.getProgram().length() > MIN_LENGTH) {
                result++;
                break;
            }
        }

        return new Integer(result);
    }

    public Integer getNumberOfFieldsFilledEn() {
        int numberOfFieldsFilled = 0;

        if (!getInfoLecturingTeachers().isEmpty()) {
            numberOfFieldsFilled++;
        }

        Iterator iter = getExecutionCourse().getAssociatedBibliographicReferences().iterator();
        while (iter.hasNext()) {
            BibliographicReference bibliographicReference = (BibliographicReference) iter.next();
            if (bibliographicReference.getTitle().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }
        if (getExecutionCourse().getEvaluationMethod() != null
                && getExecutionCourse().getEvaluationMethod().getEvaluationElements() != null
                && getExecutionCourse().getEvaluationMethod().getEvaluationElements().getContent(MultiLanguageString.en) != null
                && getExecutionCourse().getEvaluationMethod().getEvaluationElements().getContent(MultiLanguageString.en).length() > MIN_LENGTH) {
            numberOfFieldsFilled++;
        }

        iter = getInfoCurriculums().iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getGeneralObjectivesEn() != null && infoCurriculum.getGeneralObjectivesEn().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }

        iter = getInfoCurriculums().iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getOperacionalObjectivesEn() != null
                    && infoCurriculum.getOperacionalObjectivesEn().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }

        iter = getInfoCurriculums().iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getProgramEn() != null && infoCurriculum.getProgramEn().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }

        return Integer.valueOf(numberOfFieldsFilled);
    }

    public Date getLastModificationDate() {
        final Set<DateTime> dates = new HashSet<DateTime>();

        if (getExecutionCourse().hasCourseReport()) {
            dates.add(getExecutionCourse().getCourseReport().getLastModificationDateDateTime());
        }

        for (final Curriculum curriculum : getExecutionCourse().getCurriculums(getExecutionYear())) {
            dates.add(curriculum.getLastModificationDateDateTime());
        }

        return dates.isEmpty() ? null : Collections.<DateTime> max(dates).toDate();
    }

    public List<InfoDepartment> getInfoDepartments() {
        final Set<Teacher> responsibleForTeachers = new HashSet<Teacher>();
        for (final Professorship responsibleFor : getExecutionCourse().responsibleFors()) {
            responsibleForTeachers.add(responsibleFor.getTeacher());
        }

        final List<InfoDepartment> result = new ArrayList<InfoDepartment>();

        for (final Teacher teacher : responsibleForTeachers) {
            result.add(InfoDepartment.newInfoFromDomain(teacher.getCurrentWorkingDepartment()));
        }

        return result;
    }

    public List<InfoCurriculum> getInfoCurriculums() {
        final List<InfoCurriculum> result = new ArrayList<InfoCurriculum>();

        for (final Curriculum curriculum : getExecutionCourse().getCurriculums(getExecutionYear())) {
            final InfoCurriculum infoCurriculum = InfoCurriculum.newInfoFromDomain(curriculum);

            final InfoCurricularCourse infoCurricularCourse =
                    InfoCurricularCourse.newInfoFromDomain(curriculum.getCurricularCourse());
            infoCurricularCourse.setInfoScopes(getInfoScopes(curriculum.getCurricularCourse()));
            infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);

            result.add(infoCurriculum);
        }

        return result;
    }

    private List<InfoCurricularCourseScope> getInfoScopes(final CurricularCourse curricularCourse) {
        final List<InfoCurricularCourseScope> result = new ArrayList<InfoCurricularCourseScope>();

        for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopesSet()) {
            result.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
        }

        return result;
    }

    public List<InfoLesson> getInfoLessons() {
        final List<InfoLesson> result = new ArrayList<InfoLesson>();

        for (final Lesson lesson : getExecutionCourse().getLessons()) {
            result.add(InfoLesson.newInfoFromDomain(lesson));
        }

        return getFilteredInfoLessons(result);
    }

    private List<InfoLesson> getFilteredInfoLessons(final List<InfoLesson> infoLessons) {
        final List<InfoLesson> result = new ArrayList<InfoLesson>();

        InfoLesson infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.TEORICA);
        if (infoLesson != null) {
            result.add(infoLesson);
        }

        infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.PRATICA);
        if (infoLesson != null) {
            result.add(infoLesson);
        }

        infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.LABORATORIAL);
        if (infoLesson != null) {
            result.add(infoLesson);
        }

        infoLesson = getFilteredInfoLessonByType(infoLessons, ShiftType.TEORICO_PRATICA);
        if (infoLesson != null) {
            result.add(infoLesson);
        }

        return result;
    }

    private InfoLesson getFilteredInfoLessonByType(List<InfoLesson> infoLessons, ShiftType type) {
        final ShiftType lessonType = type;
        InfoLesson infoLesson = (InfoLesson) CollectionUtils.find(infoLessons, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                InfoLesson infoLesson = (InfoLesson) o;
                if (infoLesson.getInfoShift().getShift().getCourseLoads().size() == 1) {
                    return infoLesson.getInfoShift().getShift().containsType(lessonType);
                }
                return false;
            }
        });
        return infoLesson;
    }

    public List<InfoSiteEvaluationInformation> getInfoSiteEvaluationInformations() {
        final List<InfoSiteEvaluationInformation> result = new ArrayList<InfoSiteEvaluationInformation>();

        final ExecutionSemester executionSemester = getExecutionCourse().getExecutionPeriod();
        for (final CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            final InfoSiteEvaluationInformation infoSiteEvaluationInformation = new InfoSiteEvaluationInformation();

            final InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
            final List<Enrolment> enrolled = curricularCourse.getEnrolmentsByExecutionPeriod(executionSemester);
            infoSiteEvaluationStatistics.setEnrolled(enrolled.size());
            infoSiteEvaluationStatistics.setEvaluated(Enrolment.countEvaluated(enrolled));
            infoSiteEvaluationStatistics.setApproved(Enrolment.countApproved(enrolled));
            infoSiteEvaluationStatistics.setInfoExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(executionSemester));

            infoSiteEvaluationInformation.setInfoSiteEvaluationStatistics(infoSiteEvaluationStatistics);
            infoSiteEvaluationInformation.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
            infoSiteEvaluationInformation.setInfoSiteEvaluationHistory(getInfoSiteEvaluationsHistory(executionSemester,
                    curricularCourse));

            result.add(infoSiteEvaluationInformation);
        }

        return result;
    }

    private List<InfoSiteEvaluationStatistics> getInfoSiteEvaluationsHistory(final ExecutionSemester executionPeriodToTest,
            final CurricularCourse curricularCourse) {
        final List<InfoSiteEvaluationStatistics> result = new ArrayList<InfoSiteEvaluationStatistics>();

        final Set<ExecutionSemester> executionSemesters = new HashSet<ExecutionSemester>();
        for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();

            // filter the executionPeriods by semester;
            // also, information regarding execution years after the course's
            // execution year must not be shown
            if (executionSemester.getSemester().equals(executionPeriodToTest.getSemester())
                    && executionSemester.getExecutionYear().isBefore(executionPeriodToTest.getExecutionYear())) {
                executionSemesters.add(executionSemester);
            }
        }

        for (final ExecutionSemester executionSemester : executionSemesters) {
            final InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
            infoSiteEvaluationStatistics.setInfoExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(executionSemester));

            final List<Enrolment> enrolled = curricularCourse.getEnrolmentsByExecutionPeriod(executionSemester);
            infoSiteEvaluationStatistics.setEnrolled(enrolled.size());
            infoSiteEvaluationStatistics.setEvaluated(Enrolment.countEvaluated(enrolled));
            infoSiteEvaluationStatistics.setApproved(Enrolment.countApproved(enrolled));

            result.add(infoSiteEvaluationStatistics);
        }

        Collections.sort(result, new ReverseComparator(new BeanComparator("infoExecutionPeriod.infoExecutionYear.year")));

        return result;
    }

    public String getEvaluationMethod() {
        final ExecutionCourse executionCourse = getExecutionCourse();
        if (executionCourse != null) {
            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
            for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
                final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
                if (curricularCourse.isActive(executionSemester) && competenceCourse != null) {
                    final CompetenceCourseInformation competenceCourseInformation =
                            competenceCourse.findCompetenceCourseInformationForExecutionPeriod(executionSemester);
                    if (competenceCourseInformation != null) {
                        competenceCourseInformation.getEvaluationMethod();
                    }
                }
            }
        }
        return null;
    }

}
