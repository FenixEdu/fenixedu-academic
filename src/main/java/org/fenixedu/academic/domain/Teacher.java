/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.messaging.Forum;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.util.PeriodState;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class Teacher extends Teacher_Base {

    public static final Comparator<Teacher> TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER = new Comparator<Teacher>() {

        @Override
        public int compare(Teacher teacher1, Teacher teacher2) {
            final int teacherIdCompare = teacher1.getPerson().getUsername().compareTo(teacher2.getPerson().getUsername());

            if (teacher1.getLastCategory() == null && teacher2.getLastCategory() == null) {
                return teacherIdCompare;
            } else if (teacher1.getLastCategory() == null) {
                return 1;
            } else if (teacher2.getLastCategory() == null) {
                return -1;
            } else {
                final int categoryCompare = teacher1.getLastCategory().compareTo(teacher2.getLastCategory());
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

    /**
     * Gets the latest department of the teacher for the given interval
     * 
     * @param interval the time frame to consider
     * @return an {@code Optional} of the department.
     */
    public Optional<Department> getDepartment(AcademicInterval interval) {
        return getTeacherAuthorization(interval).map(a -> a.getDepartment());
    }

    /**
     * Same as {@link #getDepartment(AcademicInterval)} for the current semester
     * 
     * @return The department or null
     */
    public Department getDepartment() {
        return getDepartment(AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER)).orElse(null);
    }

    /**
     * Gets the last department the teacher had up to the given interval (inclusive). Useful when we don't want to consider
     * authorization interruptions, and a teacher once belonging to a department stays with that status.
     * 
     * @param interval the time frame to consider
     * @return an {@code Optional} of the department.
     */
    public Department getLastDepartment(AcademicInterval interval) {
        return getLastTeacherAuthorization(interval).map(a -> a.getDepartment()).orElse(null);
    }

    /**
     * Same as {@link #getLastDepartment(AcademicInterval)} for the current semester
     * 
     * @return The department or null
     */
    public Department getLastDepartment() {
        return getLastDepartment(AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER));
    }

    /**
     * Gets the latest category of the teacher in the given interval.
     * 
     * @param interval the time frame to consider
     * @return an {@code Optional} of the category.
     */
    public Optional<TeacherCategory> getCategory(AcademicInterval interval) {
        return getTeacherAuthorization(interval).map(a -> a.getTeacherCategory());
    }

    /**
     * Same as {@link #getCategory(AcademicInterval)} for the current semester
     * 
     * @return the category or null
     */
    public TeacherCategory getCategory() {
        return getCategory(AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER)).orElse(null);
    }

    /**
     * Gets the last category the teacher had up to the given interval (inclusive). Useful when we don't want to consider
     * authorization interruptions, and a teacher once having a category preserves that status.
     * 
     * @param interval the time frame to consider
     * @return an {@code Optional} of the category.
     */
    public Optional<TeacherCategory> getLastCategory(AcademicInterval interval) {
        return getLastTeacherAuthorization(interval).map(a -> a.getTeacherCategory());
    }

    /**
     * Same as {@link #getLastCategory(AcademicInterval)} for the current semester
     * 
     * @return the category or null
     */
    public TeacherCategory getLastCategory() {
        return getLastCategory(AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER)).orElse(null);
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
                if (executionCourse.functionsAt(campus)) {
                    return true;
                }
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

    public Stream<TeacherAuthorization> getRevokedTeacherAuthorizationStream() {
        return getRevokedAuthorizationSet().stream().sorted(Collections.reverseOrder());
    }

    public Stream<TeacherAuthorization> getTeacherAuthorizationStream() {
        return getAuthorizationSet().stream().sorted(Collections.reverseOrder());
    }

    public Optional<TeacherAuthorization> getTeacherAuthorization(AcademicInterval interval) {
        return getTeacherAuthorizationStream().filter(a -> a.getExecutionSemester().getAcademicInterval().equals(interval))
                .findFirst();
    }

    public Optional<TeacherAuthorization> getTeacherAuthorization() {
        return getTeacherAuthorization(AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER));
    }

    public boolean hasTeacherAuthorization(AcademicInterval interval) {
        return getTeacherAuthorization(interval).isPresent();
    }

    public boolean hasTeacherAuthorization() {
        return getTeacherAuthorization().isPresent();
    }

    protected Optional<TeacherAuthorization> getLastTeacherAuthorization(AcademicInterval interval) {
        return getTeacherAuthorizationStream().filter(a -> !a.getExecutionSemester().getAcademicInterval().isAfter(interval))
                .findFirst();
    }

    public Optional<TeacherAuthorization> getLatestTeacherAuthorizationInInterval(Interval interval) {
        return getTeacherAuthorizationStream().filter(a -> a.getExecutionSemester().getAcademicInterval().overlaps(interval))
                .findFirst();
    }

    public boolean isActiveContractedTeacher() {
        return getTeacherAuthorization().map(a -> a.isContracted()).orElse(false);
    }

    public boolean isErasmusCoordinator() {
        return !getMobilityCoordinationsSet().isEmpty();
    }

}
