package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class LoginPeriod extends LoginPeriod_Base {

    public static final Comparator<LoginPeriod> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("beginDate"), true);
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

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
	removeRootDomainObject();
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
}
