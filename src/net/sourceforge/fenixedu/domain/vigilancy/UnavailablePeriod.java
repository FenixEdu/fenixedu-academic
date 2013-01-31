package net.sourceforge.fenixedu.domain.vigilancy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class UnavailablePeriod extends UnavailablePeriod_Base {

	public UnavailablePeriod() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public UnavailablePeriod(DateTime beginDate, DateTime endDate, String justification) {
		this();
		this.setBeginDate(beginDate);
		this.setEndDate(endDate);
		this.setJustification(justification);
	}

	public UnavailablePeriod(DateTime beginDate, DateTime endDate, String justification, Person person) {
		this();
		this.setPerson(person);
		this.setBeginDate(beginDate);
		this.setEndDate(endDate);
		this.setJustification(justification);
	}

	@Override
	public void setBeginDate(DateTime begin) {
		DateTime currentTime = new DateTime();
		if ((isExamCoordinatorRequesting(this.getPerson()) || begin.isAfter(currentTime))
				&& (this.getEndDate() == null || this.getEndDate().isAfter(begin))) {
			super.setBeginDate(begin);
		} else {
			throw new DomainException("label.vigilancy.error.invalidBeginDate");
		}
	}

	@Override
	public void setEndDate(DateTime end) {
		if (this.getBeginDate() == null || this.getBeginDate().isBefore(end)) {
			super.setEndDate(end);
		} else {
			throw new DomainException("label.vigilancy.error.invalidEndDate");
		}
	}

	public Boolean containsDate(DateTime date) {
		Interval interval = new Interval(this.getBeginDate(), this.getEndDate());
		return interval.contains(date);
	}

	public Boolean containsInterval(DateTime begin, DateTime end) {
		Interval interval = new Interval(this.getBeginDate(), this.getEndDate());
		return (interval.contains(begin) || interval.contains(end));
	}

	private void doActualDelete() {
		removeRootDomainObject();
		this.getPerson().removeUnavailablePeriods(this);
		removePerson();
		super.deleteDomainObject();
	}

	public void delete() {
		if (isExamCoordinatorRequesting(this.getPerson())) {
			this.doActualDelete();
		} else {
			if (this.canChangeUnavailablePeriodFor(this.getPerson())) {
				this.doActualDelete();
			}
		}
	}

	private void doActualEdit(DateTime begin, DateTime end, String justification) {
		this.setBeginDate(begin);
		this.setEndDate(end);
		this.setJustification(justification);
	}

	public void edit(DateTime begin, DateTime end, String justification) {

		if (isExamCoordinatorRequesting(this.getPerson())) {
			this.doActualEdit(begin, end, justification);
		} else {
			if (this.canChangeUnavailablePeriodFor(this.getPerson())) {
				this.doActualEdit(begin, end, justification);
			}
		}
	}

	private boolean canChangeUnavailablePeriodFor(Person person) {
		if (person != null) {
			if (!person.isAllowedToSpecifyUnavailablePeriod()) {
				throw new DomainException("vigilancy.error.outOutPeriodToSpecifyUnavailablePeriods");
			}
			if (this.getEndDate() != null && this.getEndDate().isBeforeNow()) {
				throw new DomainException("vigilancy.error.cannotEditClosedUnavailablePeriod");
			}
			List<Vigilancy> convokes = person.getVigilanciesForYear(ExecutionYear.readCurrentExecutionYear());
			for (Vigilancy convoke : convokes) {
				WrittenEvaluation writtenEvaluation = convoke.getWrittenEvaluation();
				DateTime begin = writtenEvaluation.getBeginningDateTime();
				DateTime end = writtenEvaluation.getEndDateTime();
				if (this.containsInterval(begin, end)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private boolean isExamCoordinatorRequesting(Person person) {

		IUserView userview = AccessControl.getUserView();

		if (userview != null) {
			Person loggedPerson = userview.getPerson();
			ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

			List<VigilantGroup> vigilantGroups = person.getVigilantGroupsForExecutionYear(executionYear);

			if (loggedPerson != null) {
				ExamCoordinator coordinator = loggedPerson.getExamCoordinatorForGivenExecutionYear(executionYear);
				if (coordinator == null) {
					return false;
				} else {
					for (VigilantGroup group : vigilantGroups) {
						if (coordinator.managesGivenVigilantGroup(group)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public String getUnavailableAsString() {
		DateTime begin = this.getBeginDate();
		DateTime end = this.getEndDate();

		return String.format("%02d/%02d/%d (%02d:%02d) - %02d/%02d/%d (%02d:%02d): %s", begin.getDayOfMonth(),
				begin.getMonthOfYear(), begin.getYear(), begin.getHourOfDay(), begin.getMinuteOfHour(), end.getDayOfMonth(),
				end.getMonthOfYear(), end.getYear(), end.getHourOfDay(), end.getMinuteOfHour(), this.getJustification());
	}

}
