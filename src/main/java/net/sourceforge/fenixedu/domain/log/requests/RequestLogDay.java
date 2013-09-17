package net.sourceforge.fenixedu.domain.log.requests;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class RequestLogDay extends RequestLogDay_Base {

    public RequestLogDay() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delele() {
        ArrayList<RequestLog> rls = new ArrayList<RequestLog>();
        rls.addAll(this.getLogs());
        for (RequestLog rl : rls) {
            this.removeLogs(rl);
            rl.setDay(null);
        }
        this.setRootDomainObject(null);
        this.getMonth().removeDays(this);
        this.setNext(null);
        this.setPrevious(null);
        this.setRootDomainObjectRequestLogDay(null);
        super.deleteDomainObject();
    }

    public static RequestLogDay getToday() {
        DateTime now = new DateTime();
        int year = now.getYear();
        int month = now.getMonthOfYear();
        int day = now.getDayOfMonth();

        RequestLogDay mostRecent = RootDomainObject.getInstance().getMostRecentRequestLogDay();
        if (mostRecent.getDayOfMonth().equals(day)) {
            return mostRecent;
        } else {
            if (mostRecent.getMonth().getMonthOfYear().equals(month)) {
                RequestLogDay requestLogDay = new RequestLogDay();
                requestLogDay.setDayOfMonth(day);
                requestLogDay.setPrevious(mostRecent);
                mostRecent.getMonth().addDays(requestLogDay);
                RootDomainObject.getInstance().setMostRecentRequestLogDay(requestLogDay);
                return requestLogDay;
            } else {
                if (mostRecent.getMonth().getYear().getYear().equals(year)) {
                    RequestLogMonth requestLogMonth = new RequestLogMonth();
                    requestLogMonth.setMonthOfYear(month);
                    mostRecent.getMonth().getYear().addMonths(requestLogMonth);

                    RequestLogDay requestLogDay = new RequestLogDay();
                    requestLogDay.setDayOfMonth(day);
                    requestLogDay.setPrevious(mostRecent);
                    mostRecent.getMonth().addDays(requestLogDay);
                    RootDomainObject.getInstance().setMostRecentRequestLogDay(requestLogDay);
                    return requestLogDay;
                } else {
                    RequestLogYear requestLogYear = new RequestLogYear();
                    requestLogYear.setYear(year);

                    RequestLogMonth requestLogMonth = new RequestLogMonth();
                    requestLogMonth.setMonthOfYear(month);
                    requestLogYear.addMonths(requestLogMonth);

                    RequestLogDay requestLogDay = new RequestLogDay();
                    requestLogDay.setDayOfMonth(day);
                    requestLogDay.setPrevious(mostRecent);
                    mostRecent.getMonth().addDays(requestLogDay);
                    RootDomainObject.getInstance().setMostRecentRequestLogDay(requestLogDay);
                    return requestLogDay;
                }
            }
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.RequestLog> getLogs() {
        return getLogsSet();
    }

    @Deprecated
    public boolean hasAnyLogs() {
        return !getLogsSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObjectRequestLogDay() {
        return getRootDomainObjectRequestLogDay() != null;
    }

    @Deprecated
    public boolean hasDayOfMonth() {
        return getDayOfMonth() != null;
    }

    @Deprecated
    public boolean hasNext() {
        return getNext() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPrevious() {
        return getPrevious() != null;
    }

    @Deprecated
    public boolean hasMonth() {
        return getMonth() != null;
    }

}
