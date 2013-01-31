package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipSummaryPeriodBean;

import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class TutorshipSummaryPeriod extends TutorshipSummaryPeriod_Base {

	public TutorshipSummaryPeriod(ExecutionSemester executionSemester, LocalDate beginDate, LocalDate endDate) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setExecutionSemester(executionSemester);
		setBeginDate(beginDate);
		setEndDate(endDate);
	}

	@Service
	public void update(TutorshipSummaryPeriodBean bean) {
		setBeginDate(bean.getBeginDate());
		setEndDate(bean.getEndDate());
	}

	@Service
	public static TutorshipSummaryPeriod create(TutorshipSummaryPeriodBean bean) {
		TutorshipSummaryPeriod tutorshipSummaryPeriod =
				new TutorshipSummaryPeriod(bean.getExecutionSemester(), bean.getBeginDate(), bean.getEndDate());

		return tutorshipSummaryPeriod;
	}

	public boolean isOpenNow() {
		Interval interval =
				new Interval(getBeginDate().toDateTimeAtStartOfDay(), getEndDate().plusDays(1).toDateTimeAtStartOfDay());
		return interval.containsNow();
	}
}
