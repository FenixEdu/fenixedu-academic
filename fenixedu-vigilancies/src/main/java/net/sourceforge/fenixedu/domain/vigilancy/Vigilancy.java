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
package org.fenixedu.academic.domain.vigilancy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.AccessControl;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public abstract class Vigilancy extends Vigilancy_Base {

    protected static final int POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN = 0;

    public static final Comparator<Vigilancy> COMPARATOR_BY_WRITTEN_EVALUATION_BEGGINING = new BeanComparator(
            "writtenEvaluation.dayDateYearMonthDay");

    public static final Comparator<Vigilancy> COMPARATOR_BY_VIGILANT_CATEGORY = new Comparator<Vigilancy>() {
        @Override
        public int compare(Vigilancy o1, Vigilancy o2) {
            return VigilantWrapper.CATEGORY_COMPARATOR.compare(o1.getVigilantWrapper(), o2.getVigilantWrapper());
        }
    };

    public static final Comparator<Vigilancy> COMPARATOR_BY_VIGILANT_USERNAME = new Comparator<Vigilancy>() {
        @Override
        public int compare(Vigilancy o1, Vigilancy o2) {
            return VigilantWrapper.USERNAME_COMPARATOR.compare(o1.getVigilantWrapper(), o2.getVigilantWrapper());
        }
    };

    public static final Comparator<Vigilancy> COMPARATOR_BY_VIGILANT_SORT_CRITERIA = new Comparator<Vigilancy>() {

        @Override
        public int compare(Vigilancy o1, Vigilancy o2) {
            return VigilantWrapper.SORT_CRITERIA_COMPARATOR.compare(o1.getVigilantWrapper(), o2.getVigilantWrapper());
        }

    };

    public Vigilancy() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public VigilantGroup getAssociatedVigilantGroup() {
        Set<VigilantGroup> groups = VigilantGroup.getAssociatedVigilantGroups(this.getWrittenEvaluation());
        for (VigilantGroup group : groups) {
            if (this.getVigilantWrapper().hasVigilantGroup(group)) {
                return group;
            }
        }
        return new ArrayList<VigilantGroup>(groups).iterator().next();
    }

    @Override
    public void setStatus(AttendingStatus status) {
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        Person person = AccessControl.getPerson();
        if (this.getExecutionYear() != currentExecutionYear
                && ExamCoordinator.getExamCoordinatorForGivenExecutionYear(person, currentExecutionYear) == null) {
            throw new DomainException("vigilancy.error.notAuthorized");
        } else {
            if (AttendingStatus.ATTENDED.equals(status)) {
                setAttendedWrittenEvaluation(getWrittenEvaluation());
            }
            super.setStatus(status);
        }
    }

    @Override
    public void setActive(Boolean bool) {
        if (!canBeChangedByUser() || this.getExecutionYear() != ExecutionYear.readCurrentExecutionYear()) {
            throw new DomainException("vigilancy.error.notAuthorized");
        } else {
            if (bool == null || !bool) {
                setStatus(null);
            }
            super.setActive(bool);
        }
    }

    public void setAttendedToConvoke(Boolean bool) {
        if (!canBeChangedByUser()) {
            throw new DomainException("vigilancy.error.notAuthorized");
        } else {
            if (!bool) {
                super.setStatus(AttendingStatus.NOT_ATTENDED);
            } else {
                super.setStatus(AttendingStatus.ATTENDED);
            }
        }
    }

    public Boolean getAttendedToConvoke() {
        return AttendingStatus.ATTENDED.equals(getStatus());
    }

    public ExecutionYear getExecutionYear() {
        return this.getVigilantWrapper().getExecutionYear();
    }

    public long getBeginDate() {
        return this.getBeginDateTime().getMillis();
    }

    public long getEndDate() {
        return this.getEndDateTime().getMillis();
    }

    public DateTime getBeginDateTime() {
        return this.getWrittenEvaluation().getBeginningDateTime();
    }

    public DateTime getEndDateTime() {
        return this.getWrittenEvaluation().getEndDateTime();
    }

    public YearMonthDay getBeginYearMonthDay() {
        DateTime begin = this.getWrittenEvaluation().getBeginningDateTime();
        YearMonthDay date = new YearMonthDay(begin.getYear(), begin.getMonthOfYear(), begin.getDayOfMonth());
        return date;
    }

    public YearMonthDay getEndYearMonthDay() {
        DateTime end = this.getWrittenEvaluation().getEndDateTime();
        YearMonthDay date = new YearMonthDay(end.getYear(), end.getMonthOfYear(), end.getDayOfMonth());
        return date;
    }

    public Collection<ExecutionCourse> getAssociatedExecutionCourses() {

        return this.getWrittenEvaluation().getAssociatedExecutionCoursesSet();

    }

    public Set<String> getSitesAndGroupEmails() {
        Set<String> emails = new HashSet<String>();
        String groupEmail = getAssociatedVigilantGroup().getContactEmail();
        if (groupEmail != null) {
            emails.add(groupEmail);
        }
        for (ExecutionCourse course : getWrittenEvaluation().getAssociatedExecutionCoursesSet()) {
            String mail = course.getEmail();
            if (mail != null) {
                emails.add(mail);
            }
        }
        return emails;
    }

    public Set<Person> getTeachers() {
        return getWrittenEvaluation().getTeachers();
    }

    public void delete() {
        if (getVigilancyGroup() != null) {
            throw new DomainException("error.vigilancy.cannotDeleteVigilancyUsedInAccessControl");
        }
        setWrittenEvaluation(null);
        setRootDomainObject(null);
        setVigilantWrapper(null);
        super.deleteDomainObject();
    }

    public boolean hasPointsAttributed() {
        Collection<Vigilancy> vigilancies = this.getWrittenEvaluation().getVigilanciesSet();
        for (Vigilancy vigilancy : vigilancies) {
            if (vigilancy.isAttended()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAttended() {
        return AttendingStatus.ATTENDED.equals(getStatus());
    }

    public boolean isNotAttended() {
        return AttendingStatus.NOT_ATTENDED.equals(getStatus());
    }

    public boolean isActive() {
        return getActive();
    }

    public boolean isOtherCourseVigilancy() {
        return this.getClass().equals(OtherCourseVigilancy.class);
    }

    public boolean isOwnCourseVigilancy() {
        return this.getClass().equals(OwnCourseVigilancy.class);
    }

    protected boolean isSelfAccessing() {
        Person person = AccessControl.getPerson();
        return this.getVigilantWrapper().getPerson().equals(person);
    }

    protected boolean canBeChangedByUser() {
        Person person = AccessControl.getPerson();
        return (isExamCoordinatorForGroup(person) || isTeacherForCourse(person));
    }

    protected boolean isTeacherForCourse(Person person) {
        Teacher teacher = person.getTeacher();
        return (teacher != null && teacher.teachesAny(this.getWrittenEvaluation().getAssociatedExecutionCoursesSet()));
    }

    protected boolean isExamCoordinatorForGroup(Person person) {
        ExamCoordinator coordinator =
                ExamCoordinator.getExamCoordinatorForGivenExecutionYear(person, ExecutionYear.readCurrentExecutionYear());
        return (coordinator != null && coordinator.managesGivenVigilantGroup(getAssociatedVigilantGroup()));
    }

    public boolean isAbleToConfirmAttend() {
        return isActive() && !this.getWrittenEvaluation().getIsAfterCurrentDate();
    }

    public boolean isDismissed() {
        return AttendingStatus.DISMISSED.equals(getStatus());
    }

    protected void initStatus() {
        super.setActive(true);
        super.setStatus(AttendingStatus.NOT_ATTENDED);
    }

    /**
     * A vigilancy initialy starts as active and {@value AttendingStatus#NOT_ATTENDED}. However when a vigilant is
     * diconvoked the status becomes undefined.
     * 
     * @return <code>true</code> if the status is undefined and should not be
     *         considered
     */
    public boolean isStatusUndefined() {
        return getStatus() == null;
    }

    public abstract int getPoints();

    public abstract int getEstimatedPoints();

    public static List<Vigilancy> getVigilanciesForYear(Person person, ExecutionYear executionYear) {
        final List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (final VigilantWrapper vigilantWrapper : person.getVigilantWrappersSet()) {
            if (vigilantWrapper.getExecutionYear().equals(executionYear)) {
                vigilancies.addAll(vigilantWrapper.getVigilanciesSet());
            }
        }
        return vigilancies;
    }

    public static Person getIncompatibleVigilantPerson(Person person) {
        return person.getIncompatiblePerson() != null ? person.getIncompatiblePerson() : person.getIncompatibleVigilant();
    }

    public static void setIncompatibleVigilantPerson(Person person, Person incompatible) {
        person.setIncompatibleVigilant(incompatible);
        person.setIncompatiblePerson(null);
    }

    public static List<Vigilancy> getActiveOtherVigilancies(WrittenEvaluation writtenEvaluation) {
        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy vigilancy : writtenEvaluation.getVigilanciesSet()) {
            if (vigilancy.isOtherCourseVigilancy() && vigilancy.isActive()) {
                vigilancies.add(vigilancy);
            }
        }
        return vigilancies;
    }

    public static List<Vigilancy> getAllActiveVigilancies(WrittenEvaluation writtenEvaluation) {
        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy vigilancy : writtenEvaluation.getVigilanciesSet()) {
            if (vigilancy.isActive()) {
                vigilancies.add(vigilancy);
            }
        }
        return vigilancies;
    }

    public static List<Vigilancy> getTeachersVigilancies(WrittenEvaluation writtenEvaluation) {
        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy vigilancy : writtenEvaluation.getVigilanciesSet()) {
            if (vigilancy.isOwnCourseVigilancy()) {
                vigilancies.add(vigilancy);
            }
        }
        return vigilancies;
    }

    public static List<Vigilancy> getOthersVigilancies(WrittenEvaluation writtenEvaluation) {
        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy vigilancy : writtenEvaluation.getVigilanciesSet()) {
            if (vigilancy.isOtherCourseVigilancy()) {
                vigilancies.add(vigilancy);
            }
        }
        return vigilancies;
    }

    public static List<Vigilancy> getActiveVigilancies(WrittenEvaluation writtenEvaluation) {
        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy vigilancy : writtenEvaluation.getVigilanciesSet()) {
            if (vigilancy.isActive()) {
                vigilancies.add(vigilancy);
            }
        }
        return vigilancies;
    }
}
