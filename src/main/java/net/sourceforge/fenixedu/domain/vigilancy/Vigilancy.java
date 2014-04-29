package net.sourceforge.fenixedu.domain.vigilancy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

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
        Set<VigilantGroup> groups = this.getWrittenEvaluation().getAssociatedVigilantGroups();
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
                && person.getExamCoordinatorForGivenExecutionYear(currentExecutionYear) == null) {
            throw new DomainException("vigilancy.error.notAuthorized");
        } else {
            if (AttendingStatus.ATTENDED.equals(status)) {
                getWrittenEvaluation().fillVigilancyReport();
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

        return this.getWrittenEvaluation().getAssociatedExecutionCourses();

    }

    public Set<String> getSitesAndGroupEmails() {
        Set<String> emails = new HashSet<String>();
        String groupEmail = getAssociatedVigilantGroup().getContactEmail();
        if (groupEmail != null) {
            emails.add(groupEmail);
        }
        for (ExecutionCourse course : getWrittenEvaluation().getAssociatedExecutionCourses()) {
            String mail = course.getSite().getMail();
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
        Collection<Vigilancy> vigilancies = this.getWrittenEvaluation().getVigilancies();
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
        return (teacher != null && teacher.teachesAny(this.getWrittenEvaluation().getAssociatedExecutionCourses()));
    }

    protected boolean isExamCoordinatorForGroup(Person person) {
        ExamCoordinator coordinator = person.getExamCoordinatorForGivenExecutionYear(ExecutionYear.readCurrentExecutionYear());
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

    @Deprecated
    public boolean hasWrittenEvaluation() {
        return getWrittenEvaluation() != null;
    }

    @Deprecated
    public boolean hasStatus() {
        return getStatus() != null;
    }

    @Deprecated
    public boolean hasActive() {
        return getActive() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasVigilantWrapper() {
        return getVigilantWrapper() != null;
    }

}
