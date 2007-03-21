package net.sourceforge.fenixedu.domain.vigilancy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public abstract class Vigilancy extends Vigilancy_Base {

    protected static final int POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN = 0;

    public static final Comparator<Vigilancy> COMPARATOR_BY_WRITTEN_EVALUATION_BEGGINING = new BeanComparator(
	    "writtenEvaluation.dayDateYearMonthDay");

    public static final Comparator<Vigilancy> COMPARATOR_BY_VIGILANT_CATEGORY = new Comparator<Vigilancy>() {
	public int compare(Vigilancy o1, Vigilancy o2) {
	    return Vigilant.CATEGORY_COMPARATOR.compare(o1.getVigilant(), o2.getVigilant());
	}
    };

    public static final Comparator<Vigilancy> COMPARATOR_BY_VIGILANT_USERNAME = new Comparator<Vigilancy>() {
	public int compare(Vigilancy o1, Vigilancy o2) {
	    return Vigilant.USERNAME_COMPARATOR.compare(o1.getVigilant(), o2.getVigilant());
	}
    };

    public Vigilancy() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(getClass().getName());
    }

    public VigilantGroup getAssociatedVigilantGroup() {
	Set<VigilantGroup> groups = this.getWrittenEvaluation().getAssociatedVigilantGroups();
	for (VigilantGroup group : groups) {
	    if (this.getVigilant().hasVigilantGroup(group)) {
		return group;
	    }
	}
	return new ArrayList<VigilantGroup>(groups).get(0);
    }

    @Override
    public void setActive(Boolean bool) {
	if (!canBeChangedByUser()) {
	    throw new DomainException("vigilancy.error.notAuthorized");
	} else {
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
	return this.getVigilant().getExecutionYear();
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

    public List<ExecutionCourse> getAssociatedExecutionCourses() {

	return this.getWrittenEvaluation().getAssociatedExecutionCourses();

    }

    public static List<String> getEmailsThatShouldBeContactedFor(WrittenEvaluation writtenEvaluation) {
	List<String> emails = getEmailsBySite(writtenEvaluation);
	if (emails.size() == 0) {
	    emails = getEmailsByTeachers(writtenEvaluation);
	}
	return emails;
    }

    private static List<String> getEmailsBySite(WrittenEvaluation writtenEvaluation) {
	Set<String> emails = new HashSet<String>();
	for (ExecutionCourse course : writtenEvaluation.getAssociatedExecutionCourses()) {
	    String mail = course.getSite().getMail();
	    if (mail != null) {
		emails.add(course.getSite().getMail());
	    }
	}
	return new ArrayList<String>(emails);
    }

    private static List<String> getEmailsByTeachers(WrittenEvaluation writtenEvaluation) {
	Set<String> emails = new HashSet<String>();
	for (ExecutionCourse course : writtenEvaluation.getAssociatedExecutionCourses()) {
	    for (Professorship professorship : course.getProfessorships()) {
		String mail = professorship.getTeacher().getPerson().getEmail();
		emails.add(mail);
	    }
	}
	return new ArrayList<String>(emails);
    }

    public void delete() {
	removeWrittenEvaluation();
	removeVigilant();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean hasPointsAttributed() {
	List<Vigilancy> vigilancies = this.getWrittenEvaluation().getVigilancies();
	for (Vigilancy vigilancy : vigilancies) {
	    if (vigilancy.isAttended())
		return true;
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
	return this.getVigilant().equals(
		person.getVigilantForGivenExecutionYear(ExecutionYear.readCurrentExecutionYear()));
    }

    protected boolean canBeChangedByUser() {
	Person person = AccessControl.getPerson();
	return (isExamCoordinatorForGroup(person) || isTeacherForCourse(person));
    }

    protected boolean isTeacherForCourse(Person person) {
	Teacher teacher = person.getTeacher();
	return (teacher != null && teacher.teachesAny(this.getWrittenEvaluation()
		.getAssociatedExecutionCourses()));
    }

    protected boolean isExamCoordinatorForGroup(Person person) {
	ExamCoordinator coordinator = person.getExamCoordinatorForGivenExecutionYear(ExecutionYear
		.readCurrentExecutionYear());
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

    public abstract int getPoints();
}
