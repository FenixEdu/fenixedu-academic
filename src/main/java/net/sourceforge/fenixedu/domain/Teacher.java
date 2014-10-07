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
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

public class Teacher extends Teacher_Base {

    public static final Comparator<Teacher> TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER = new Comparator<Teacher>() {

        @Override
        public int compare(Teacher teacher1, Teacher teacher2) {
            final int teacherIdCompare = teacher1.getPerson().getUsername().compareTo(teacher2.getPerson().getUsername());

            if (teacher1.getCategory() == null && teacher2.getCategory() == null) {
                return teacherIdCompare;
            } else if (teacher1.getCategory() == null) {
                return 1;
            } else if (teacher2.getCategory() == null) {
                return -1;
            } else {
                final int categoryCompare = teacher1.getCategory().compareTo(teacher2.getCategory());
                return categoryCompare == 0 ? teacherIdCompare : categoryCompare;
            }
        }

    };

    public Teacher(Person person) {
        super();
        setPerson(person);
        setRootDomainObject(Bennu.getInstance());
    }

    public String getTeacherId() {
        return getPerson().getUsername();
    }

    public static Teacher readByIstId(String istId) {
        User user = User.findByUsername(istId);
        if (user != null) {
            return user.getPerson().getTeacher();
        } else {
            return null;
        }
    }

    @Override
    public void setPerson(Person person) {
        if (person == null) {
            throw new DomainException("error.teacher.no.person");
        }
        super.setPerson(person);
    }

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    public List<Professorship> responsibleFors() {
        final List<Professorship> result = new ArrayList<Professorship>();
        for (final Professorship professorship : this.getProfessorships()) {
            if (professorship.isResponsibleFor()) {
                result.add(professorship);
            }
        }
        return result;
    }

    public Professorship isResponsibleFor(ExecutionCourse executionCourse) {
        for (final Professorship professorship : this.getProfessorships()) {
            if (professorship.getResponsibleFor() && professorship.getExecutionCourse() == executionCourse) {
                return professorship;
            }
        }
        return null;
    }

    public void updateResponsabilitiesFor(String executionYearId, List<Integer> executionCourses) throws MaxResponsibleForExceed,
            InvalidCategory {

        if (executionYearId == null || executionCourses == null) {
            throw new NullPointerException();
        }

        boolean responsible;
        for (final Professorship professorship : this.getProfessorships()) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().getExecutionYear().getExternalId().equals(executionYearId)) {
                responsible = executionCourses.contains(executionCourse.getExternalId());
                if (!professorship.getResponsibleFor().equals(Boolean.valueOf(responsible))) {
                    ResponsibleForValidator.getInstance().validateResponsibleForList(this, executionCourse, professorship);
                    professorship.setResponsibleFor(responsible);
                }
            }
        }
    }

    public Unit getCurrentWorkingUnit() {
        Employee employee = this.getPerson().getEmployee();
        return (employee != null) ? employee.getCurrentWorkingPlace() : null;
    }

    public Unit getLastWorkingUnit() {
        Employee employee = this.getPerson().getEmployee();
        return (employee != null) ? employee.getLastWorkingPlace() : null;
    }

    public Unit getLastWorkingUnit(YearMonthDay begin, YearMonthDay end) {
        Employee employee = this.getPerson().getEmployee();
        return (employee != null) ? employee.getLastWorkingPlace(begin, end) : null;
    }

    public Department getCurrentWorkingDepartment() {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            Department currentDepartmentWorkingPlace = employee.getCurrentDepartmentWorkingPlace();
            if (currentDepartmentWorkingPlace != null) {
                return currentDepartmentWorkingPlace;
            }
        }

        TeacherAuthorization teacherAuthorization = getTeacherAuthorization(ExecutionSemester.readActualExecutionSemester());
        return teacherAuthorization != null && teacherAuthorization instanceof ExternalTeacherAuthorization ? ((ExternalTeacherAuthorization) teacherAuthorization)
                .getDepartment() : null;
    }

    public Department getLastWorkingDepartment(YearMonthDay begin, YearMonthDay end) {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            Department lastDepartmentWorkingPlace = employee.getLastDepartmentWorkingPlace(begin, end);
            if (lastDepartmentWorkingPlace != null) {
                return lastDepartmentWorkingPlace;
            }

        }
        List<ExecutionSemester> executionSemesters =
                ExecutionSemester.readExecutionPeriodsInTimePeriod(begin.toLocalDate(), end.toLocalDate());
        Collections.sort(executionSemesters, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));
        for (ExecutionSemester executionSemester : executionSemesters) {
            TeacherAuthorization teacherAuthorization = getTeacherAuthorization(executionSemester);
            if (teacherAuthorization != null && teacherAuthorization instanceof ExternalTeacherAuthorization) {
                return ((ExternalTeacherAuthorization) teacherAuthorization).getDepartment();
            }
        }
        return null;
    }

    public Department getLastWorkingDepartment() {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            Department lastDepartmentWorkingPlace = employee.getLastDepartmentWorkingPlace();
            if (lastDepartmentWorkingPlace != null) {
                return lastDepartmentWorkingPlace;
            }
        }

        TeacherAuthorization teacherAuthorization = getLastTeacherAuthorization();
        return teacherAuthorization != null && teacherAuthorization instanceof ExternalTeacherAuthorization ? ((ExternalTeacherAuthorization) teacherAuthorization)
                .getDepartment() : null;
    }

    public List<Unit> getWorkingPlacesByPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
        List<Unit> workingPlaces = new ArrayList<Unit>();
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            workingPlaces.addAll(employee.getWorkingPlaces(beginDate, endDate));
        }

        for (TeacherAuthorization ta : getAuthorizationSet()) {

            if (ta instanceof ExternalTeacherAuthorization && ((ExternalTeacherAuthorization) ta).getActive()
                    && ta.getExecutionSemester().isInTimePeriod(beginDate, endDate)
                    && !workingPlaces.contains(((ExternalTeacherAuthorization) ta).getDepartment().getDepartmentUnit())) {
                workingPlaces.add(((ExternalTeacherAuthorization) ta).getDepartment().getDepartmentUnit());
            }
        }

        return workingPlaces;
    }

    public ProfessionalCategory getCategory() {
        ProfessionalCategory category = getCurrentCategory();
        if (category == null) {
            PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
            return personProfessionalData == null ? null : personProfessionalData
                    .getLastProfessionalCategoryByCategoryType(CategoryType.TEACHER);
        }
        return category;
    }

    public ProfessionalCategory getCurrentCategory() {
        ProfessionalCategory professionalCategory = null;
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            professionalCategory =
                    personProfessionalData.getProfessionalCategoryByCategoryType(CategoryType.TEACHER, new LocalDate());
        }
        if (professionalCategory == null) {
            TeacherAuthorization teacherAuthorization = getTeacherAuthorization(ExecutionSemester.readActualExecutionSemester());
            if (teacherAuthorization != null) {
                professionalCategory = teacherAuthorization.getProfessionalCategory();
            }
        }
        return professionalCategory;
    }

    public ProfessionalCategory getLastCategory(LocalDate begin, LocalDate end) {
        ProfessionalCategory professionalCategory = null;
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            professionalCategory =
                    personProfessionalData.getLastProfessionalCategoryByCategoryType(CategoryType.TEACHER, begin, end);
        }
        if (professionalCategory == null) {
            List<ExecutionSemester> executionSemesters = ExecutionSemester.readExecutionPeriodsInTimePeriod(begin, end);
            Collections.sort(executionSemesters, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));
            for (ExecutionSemester executionSemester : executionSemesters) {
                TeacherAuthorization teacherAuthorization = getTeacherAuthorization(executionSemester);
                if (teacherAuthorization != null && teacherAuthorization.getProfessionalCategory() != null) {
                    return teacherAuthorization.getProfessionalCategory();
                }
            }
        }
        return professionalCategory;
    }

    public ProfessionalCategory getCategoryByPeriod(ExecutionSemester executionSemester) {
        OccupationPeriod lessonsPeriod = executionSemester.getLessonsPeriod();
        return getLastCategory(lessonsPeriod.getStartYearMonthDay().toLocalDate(), lessonsPeriod
                .getEndYearMonthDayWithNextPeriods().toLocalDate());
    }

    public PersonContractSituation getCurrentTeacherContractSituation() {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        return personProfessionalData != null ? personProfessionalData
                .getCurrentPersonContractSituationByCategoryType(CategoryType.TEACHER) : null;
    }

    public PersonContractSituation getCurrentOrLastTeacherContractSituation() {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        return personProfessionalData != null ? personProfessionalData
                .getCurrentOrLastPersonContractSituationByCategoryType(CategoryType.TEACHER) : null;
    }

    public PersonContractSituation getCurrentOrLastTeacherContractSituation(LocalDate begin, LocalDate end) {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        return personProfessionalData != null ? personProfessionalData.getCurrentOrLastPersonContractSituationByCategoryType(
                CategoryType.TEACHER, begin, end) : null;
    }

    public PersonContractSituation getDominantTeacherContractSituation(Interval interval) {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        return personProfessionalData != null ? personProfessionalData.getDominantPersonContractSituationByCategoryType(
                CategoryType.TEACHER, interval) : null;
    }

    public boolean hasAnyTeacherContractSituation() {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            return !personProfessionalData.getPersonContractSituationsByCategoryType(CategoryType.TEACHER).isEmpty();
        }
        return false;
    }

    public boolean hasAnyTeacherContractSituation(LocalDate beginDate, LocalDate endDate) {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            for (PersonContractSituation personContractSituation : personProfessionalData
                    .getPersonContractSituationsByCategoryType(CategoryType.TEACHER)) {
                if (personContractSituation.betweenDates(beginDate, endDate)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<ExecutionCourse> getLecturedExecutionCoursesByExecutionYear(ExecutionYear executionYear) {
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            executionCourses.addAll(getLecturedExecutionCoursesByExecutionPeriod(executionSemester));
        }
        return executionCourses;
    }

    public List<ExecutionCourse> getLecturedExecutionCoursesByExecutionPeriod(final ExecutionSemester executionSemester) {
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (Professorship professorship : getProfessorships()) {
            ExecutionCourse executionCourse = professorship.getExecutionCourse();

            if (executionCourse.getExecutionPeriod().equals(executionSemester)) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    public List<ExecutionCourse> getAllLecturedExecutionCourses() {
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (Professorship professorship : this.getProfessorships()) {
            executionCourses.add(professorship.getExecutionCourse());
        }
        return executionCourses;
    }

    public Professorship getProfessorshipByExecutionCourse(final ExecutionCourse executionCourse) {
        return (Professorship) CollectionUtils.find(getProfessorships(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                Professorship professorship = (Professorship) arg0;
                return professorship.getExecutionCourse() == executionCourse;
            }
        });
    }

    public boolean hasProfessorshipForExecutionCourse(final ExecutionCourse executionCourse) {
        return (getProfessorshipByExecutionCourse(executionCourse) != null);
    }

    public List<Professorship> getDegreeProfessorshipsByExecutionPeriod(final ExecutionSemester executionSemester) {
        return (List<Professorship>) CollectionUtils.select(getProfessorships(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                Professorship professorship = (Professorship) arg0;
                return professorship.getExecutionCourse().getExecutionPeriod() == executionSemester
                        && !professorship.getExecutionCourse().isMasterDegreeDFAOrDEAOnly();
            }
        });
    }

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    public List<PersonFunction> getPersonFuntions(YearMonthDay beginDate, YearMonthDay endDate) {
        return getPerson().getPersonFuntions(beginDate, endDate);
    }

    public Set<PersonContractSituation> getValidTeacherServiceExemptions(Interval interval) {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            return personProfessionalData.getValidPersonProfessionalExemptionByCategoryType(CategoryType.TEACHER, interval);
        }
        return new HashSet<PersonContractSituation>();
    }

    public PersonContractSituation getDominantTeacherServiceExemption(ExecutionSemester executionSemester) {
        PersonContractSituation dominantExemption = null;
        int daysInDominantExemption = 0;
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        for (PersonContractSituation personContractSituation : getValidTeacherServiceExemptions(executionSemester)) {
            int daysInInterval = personContractSituation.getDaysInInterval(semesterInterval);
            if (dominantExemption == null || daysInInterval > daysInDominantExemption) {
                dominantExemption = personContractSituation;
                daysInDominantExemption = daysInInterval;
            }
        }

        return dominantExemption;
    }

    public Set<PersonContractSituation> getValidTeacherServiceExemptions(ExecutionSemester executionSemester) {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        if (semesterInterval != null && personProfessionalData != null) {
            return personProfessionalData.getValidPersonProfessionalExemptionByCategoryType(CategoryType.TEACHER,
                    semesterInterval);
        }
        return new HashSet<PersonContractSituation>();
    }

    public boolean isActive() {
        PersonContractSituation situation = getCurrentTeacherContractSituation();
        return situation != null && situation.isActive(new LocalDate());
    }

    public boolean isActiveForSemester(ExecutionSemester executionSemester) {
        int minimumWorkingDays = 90;
        int activeDays = 0;
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            GiafProfessionalData giafProfessionalData = personProfessionalData.getGiafProfessionalData();
            if (giafProfessionalData != null) {
                for (final PersonContractSituation situation : giafProfessionalData.getValidPersonContractSituations()) {
                    if (situation.overlaps(semesterInterval) && situation.getProfessionalCategory() != null
                            && situation.getProfessionalCategory().getCategoryType().equals(CategoryType.TEACHER)) {
                        LocalDate beginDate =
                                situation.getBeginDate().isBefore(semesterInterval.getStart().toLocalDate()) ? semesterInterval
                                        .getStart().toLocalDate() : situation.getBeginDate();
                        LocalDate endDate =
                                situation.getEndDate() == null
                                        || situation.getEndDate().isAfter(semesterInterval.getEnd().toLocalDate()) ? semesterInterval
                                        .getEnd().toLocalDate() : situation.getEndDate();
                        int days =
                                new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay()).toPeriod(
                                        PeriodType.days()).getDays() + 1;
                        activeDays = activeDays + days;
                    }
                }
            }
        }
        return activeDays >= minimumWorkingDays;
    }

    public boolean isActiveOrHasAuthorizationForSemester(ExecutionSemester executionSemester) {
        if (isActiveForSemester(executionSemester) || getTeacherAuthorization(executionSemester) != null) {
            return true;
        }
        return false;
    }

    public boolean isInactive(ExecutionSemester executionSemester) {
        return !isActiveForSemester(executionSemester);
    }

    public boolean isMonitor(ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(executionSemester);
            return (category != null && category.isTeacherMonitorCategory());
        }
        return false;
    }

    public boolean isAssistant(ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(executionSemester);
            return (category != null && category.isTeacherAssistantCategory());
        }
        return false;
    }

    public boolean isTeacherCareerCategory(ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(executionSemester);
            return (category != null && category.isTeacherCareerCategory());
        }
        return false;
    }

    public boolean isTeacherProfessorCategory(ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(executionSemester);
            return (category != null && category.isTeacherProfessorCategory());
        }
        return false;
    }

    public List<PersonFunction> getManagementFunctions(ExecutionSemester executionSemester) {
        List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
        for (PersonFunction personFunction : this.getPerson().getPersonFunctions()) {
            if (personFunction.belongsToPeriod(executionSemester.getBeginDateYearMonthDay(),
                    executionSemester.getEndDateYearMonthDay())) {
                personFunctions.add(personFunction);
            }
        }
        return personFunctions;
    }

    public static Teacher readTeacherByUsername(final String userName) {
        final Person person = Person.readPersonByUsername(userName);
        return (person.getTeacher() != null) ? person.getTeacher() : null;
    }

    public static List<Teacher> readByNumbers(Collection<String> teacherId) {
        List<Teacher> selectedTeachers = new ArrayList<Teacher>();
        for (final Teacher teacher : Bennu.getInstance().getTeachersSet()) {
            if (teacherId.contains(teacher.getPerson().getUsername())) {
                selectedTeachers.add(teacher);
            }
            // This isn't necessary, its just a fast optimization.
            if (teacherId.size() == selectedTeachers.size()) {
                break;
            }
        }
        return selectedTeachers;
    }

    public List<Professorship> getProfessorships(ExecutionSemester executionSemester) {
        return getPerson().getProfessorships(executionSemester);
    }

    public List<Professorship> getProfessorships(ExecutionYear executionYear) {
        return getPerson().getProfessorships(executionYear);
    }

    public SortedSet<ExecutionCourse> getCurrentExecutionCourses() {
        final SortedSet<ExecutionCourse> executionCourses =
                new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);
        final ExecutionSemester currentExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        final ExecutionSemester previousExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();
        for (final Professorship professorship : getProfessorshipsSet()) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
            if (executionSemester == currentExecutionPeriod || executionSemester == previousExecutionPeriod) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    public boolean isResponsibleFor(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
        for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
            if (executionCourse.getExecutionPeriod() == executionSemester) {
                if (isResponsibleFor(executionCourse) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean teachesAny(final Collection<ExecutionCourse> executionCourses, ExecutionYear executionYear) {
        for (final Professorship professorship : getProfessorships(executionYear)) {
            if (executionCourses.contains(professorship.getExecutionCourse())) {
                return true;
            }
        }
        return false;
    }

    public boolean teachesAny(final Collection<ExecutionCourse> executionCourses) {
        return getPerson().teachesAny(executionCourses);
    }

    public void delete() {
        super.setPerson(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public boolean hasLessons(DateTime begin, DateTime end) {
        return hasLessons(begin, end, ExecutionYear.readCurrentExecutionYear());
    }

    public boolean hasLessons(DateTime begin, DateTime end, ExecutionYear executionYear) {
        final Interval interval = new Interval(begin, end);
        for (Professorship professorship : getProfessorships(executionYear)) {
            Set<Shift> associatedShifts = professorship.getExecutionCourse().getAssociatedShifts();
            for (Shift shift : associatedShifts) {
                Collection<Lesson> associatedLessons = shift.getAssociatedLessonsSet();
                for (Lesson lesson : associatedLessons) {
                    if (lesson.contains(interval)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Unit getCurrentSectionOrScientificArea() {
        final Employee employee = getPerson().getEmployee();
        if (employee != null) {
            final Unit unit = employee.getCurrentSectionOrScientificArea();
            if (unit != null) {
                return unit;
            }
        }

        final TeacherAuthorization teacherAuthorization =
                getTeacherAuthorization(ExecutionSemester.readActualExecutionSemester());
        if (teacherAuthorization != null && teacherAuthorization instanceof ExternalTeacherAuthorization) {
            final ExternalTeacherAuthorization externalTeacherAuthorization = (ExternalTeacherAuthorization) teacherAuthorization;
            final Department department = externalTeacherAuthorization.getDepartment();
            return department.getDepartmentUnit();
        }
        return null;
    }

    public List<Tutorship> getActiveTutorshipsByStudentsEntryYearAndDegree(ExecutionYear entryYear, Degree degree) {
        return getTutorshipsByStudentsEntryYearAndDegree(this.getActiveTutorships(), entryYear, degree);
    }

    public List<Tutorship> getPastTutorshipsByStudentsEntryYearAndDegree(ExecutionYear entryYear, Degree degree) {
        return getTutorshipsByStudentsEntryYearAndDegree(this.getPastTutorships(), entryYear, degree);
    }

    private List<Tutorship> getTutorshipsByStudentsEntryYearAndDegree(List<Tutorship> tutorshipsList, ExecutionYear entryYear,
            Degree degree) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : tutorshipsList) {
            StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
            ExecutionYear studentEntryYear =
                    ExecutionYear.getExecutionYearByDate(studentCurricularPlan.getRegistration().getStartDate());
            if (studentEntryYear.equals(entryYear) && studentCurricularPlan.getDegree().equals(degree)
                    && !studentCurricularPlan.getRegistration().isCanceled()) {
                tutorships.add(tutorship);
            }
        }

        return tutorships;
    }

    public List<Tutorship> getPastTutorships() {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : getTutorshipsSet()) {
            if (!tutorship.isActive()) {
                tutorships.add(tutorship);
            }
        }
        return tutorships;
    }

    public List<Tutorship> getActiveTutorships() {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : getTutorshipsSet()) {
            if (tutorship.isActive()) {
                tutorships.add(tutorship);
            }
        }
        return tutorships;
    }

    public List<Tutorship> getActiveTutorships(AcademicInterval semester) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : getTutorshipsSet()) {
            if (tutorship.isActive(semester)) {
                tutorships.add(tutorship);
            }
        }
        return tutorships;
    }

    public Integer getNumberOfPastTutorships() {
        return this.getPastTutorships().size();
    }

    public Integer getNumberOfActiveTutorships() {
        return this.getActiveTutorships().size();
    }

    public Integer getNumberOfTutorships() {
        return this.getTutorshipsSet().size();
    }

    public boolean canBeTutorOfDepartment(Department department) {
        if (getCurrentWorkingDepartment() != null && getCurrentWorkingDepartment().equals(department)) {
            return true;
        }
        return false;
    }

    public List<Tutorship> getTutorshipsByStudentsEntryYear(ExecutionYear entryYear) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();

        for (Tutorship tutorship : this.getTutorshipsSet()) {
            StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
            ExecutionYear studentEntryYear =
                    ExecutionYear.getExecutionYearByDate(studentCurricularPlan.getRegistration().getStartDate());
            if (studentEntryYear.equals(entryYear)) {
                tutorships.add(tutorship);
            }
        }

        return tutorships;
    }

    public List<Tutorship> getActiveTutorshipsByStudentsEntryYear(ExecutionYear entryYear) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();

        for (Tutorship tutorship : this.getTutorshipsSet()) {
            StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
            ExecutionYear studentEntryYear =
                    ExecutionYear.getExecutionYearByDate(studentCurricularPlan.getRegistration().getStartDate());
            if (studentEntryYear.equals(entryYear) && tutorship.isActive()) {
                tutorships.add(tutorship);
            }
        }

        return tutorships;
    }

    public Collection<? extends Forum> getForuns(final ExecutionSemester executionSemester) {
        final Collection<Forum> res = new HashSet<Forum>();
        for (Professorship professorship : getProfessorshipsSet()) {
            if (professorship.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                res.addAll(professorship.getExecutionCourse().getForuns());
            }
        }
        return res;
    }

    public boolean teachesAt(final Space campus) {
        for (final Professorship professorship : getProfessorshipsSet()) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
                return executionCourse.functionsAt(campus);
            }
        }
        return false;
    }

    public Set<Professorship> getProfessorshipsSet() {
        return getPerson().getProfessorshipsSet();
    }

    public void addProfessorships(Professorship professorship) {
        getPerson().addProfessorships(professorship);
    }

    public void removeProfessorships(Professorship professorship) {
        getPerson().removeProfessorships(professorship);
    }

    public Collection<Professorship> getProfessorships() {
        return getPerson().getProfessorshipsSet();
    }

    public Iterator<Professorship> getProfessorshipsIterator() {
        return getPerson().getProfessorshipsSet().iterator();
    }

    public boolean hasTeacherAuthorization(ExecutionSemester executionSemester) {
        for (TeacherAuthorization ta : getAuthorizationSet()) {
            if (ta instanceof ExternalTeacherAuthorization && ((ExternalTeacherAuthorization) ta).getActive()
                    && ((ExternalTeacherAuthorization) ta).getExecutionSemester().equals(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public TeacherAuthorization getTeacherAuthorization(ExecutionSemester executionSemester) {
        for (TeacherAuthorization ta : getAuthorizationSet()) {
            if (ta instanceof ExternalTeacherAuthorization && ((ExternalTeacherAuthorization) ta).getActive()
                    && ((ExternalTeacherAuthorization) ta).getExecutionSemester().equals(executionSemester)) {
                return ta;
            } else if (ta instanceof AplicaTeacherAuthorization) {
                return ta;
            }
        }
        return null;
    }

    public TeacherAuthorization getLastTeacherAuthorization() {
        LocalDate today = new LocalDate();
        TeacherAuthorization lastTeacherAuthorization = null;
        for (TeacherAuthorization ta : getAuthorizationSet()) {
            if ((lastTeacherAuthorization == null || ta.getExecutionSemester().getEndDateYearMonthDay()
                    .isAfter(lastTeacherAuthorization.getExecutionSemester().getEndDateYearMonthDay()))
                    && ta.getExecutionSemester().getEndDateYearMonthDay().isAfter(today)
                    && (ta instanceof AplicaTeacherAuthorization || ((ExternalTeacherAuthorization) ta).getActive())) {
                lastTeacherAuthorization = ta;
            }
        }
        return lastTeacherAuthorization;
    }

    public boolean isErasmusCoordinator() {
        return !getMobilityCoordinationsSet().isEmpty();
    }

    public boolean hasTutorshipIntentionFor(ExecutionDegree executionDegree) {
        for (TutorshipIntention intention : getTutorshipIntentionSet()) {
            if (intention.getAcademicInterval().equals(executionDegree.getAcademicInterval())
                    && intention.getDegreeCurricularPlan().equals(executionDegree.getDegreeCurricularPlan())) {
                return true;
            }
        }
        return false;
    }
}
