package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.FenixFramework;

public class LoginPeriod extends LoginPeriod_Base {

    public static final Comparator<LoginPeriod> COMPARATOR_BY_BEGIN_DATE = new Comparator<LoginPeriod>() {

        @Override
        public int compare(LoginPeriod o1, LoginPeriod o2) {
            final int c = o1.getBeginDate().compareTo(o2.getBeginDate());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }

    };

    public LoginPeriod(YearMonthDay begin, YearMonthDay end, Login login) {
        super();
        setBeginDate(begin);
        setEndDate(end);
        setLogin(login);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public LoginPeriod(YearMonthDay begin, Login login) {
        super();
        setBeginDate(begin);
        setLogin(login);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void edit(YearMonthDay begin, YearMonthDay end) {
        setBeginDate(begin);
        setEndDate(end);
    }

    public void delete() {
        super.setLogin(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    public void setLogin(Login login) {
        if (login == null) {
            throw new DomainException("error.LoginPeriods.empty.login");
        }
        super.setLogin(login);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
        final YearMonthDay start = getBeginDate();
        final YearMonthDay end = getEndDate();
        return start != null && (end == null || !start.isAfter(end));
    }
    @Deprecated
    public boolean hasLogin() {
        return getLogin() != null;
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

}
