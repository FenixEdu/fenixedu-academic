package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipSummaryPeriodBean;

import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class TutorshipSummaryPeriod extends TutorshipSummaryPeriod_Base {

    public TutorshipSummaryPeriod(ExecutionSemester executionSemester, LocalDate beginDate, LocalDate endDate) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setExecutionSemester(executionSemester);
        setBeginDate(beginDate);
        setEndDate(endDate);
    }

    @Atomic
    public void update(TutorshipSummaryPeriodBean bean) {
        setBeginDate(bean.getBeginDate());
        setEndDate(bean.getEndDate());
    }

    @Atomic
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
    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndDate() {
        return getEndDate() != null;
    }

    @Deprecated
    public boolean hasBeginDate() {
        return getBeginDate() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

}
