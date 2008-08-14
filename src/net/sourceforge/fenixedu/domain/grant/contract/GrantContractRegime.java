package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class GrantContractRegime extends GrantContractRegime_Base {

    public final static Comparator<GrantContractRegime> BEGIN_DATE_CONTRACT_COMPARATOR = new ComparatorChain();
    static {
	((ComparatorChain) BEGIN_DATE_CONTRACT_COMPARATOR).addComparator(new BeanComparator("dateBeginContract"));
	((ComparatorChain) BEGIN_DATE_CONTRACT_COMPARATOR).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public GrantContractRegime() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeRootDomainObject();
	removeGrantContract();
	removeGrantCostCenter();
	removeTeacher();
	super.deleteDomainObject();
    }

    public Boolean getContractRegimeActive() {
	return isActive();
    }

    public boolean belongsToPeriod(Date beginDate, Date endDate) {
	return !getDateBeginContract().after(endDate) && !getDateEndContract().before(beginDate);
    }

    public boolean belongsToPeriod(YearMonthDay begin, YearMonthDay end) {
	return !getDateBeginContractYearMonthDay().isAfter(end) && !getDateEndContractYearMonthDay().isBefore(begin);
    }

    public boolean isActive(YearMonthDay currentDate) {
	return belongsToPeriod(currentDate, currentDate);
    }

    public boolean isActive() {
	return isActive(new YearMonthDay());
    }

    public void editTimeInterval(Date dateBeginContract, Date dateEndContract) {
	super.setDateBeginContractYearMonthDay(YearMonthDay.fromDateFields(dateBeginContract));
	super.setDateEndContractYearMonthDay(YearMonthDay.fromDateFields(dateEndContract));

	addNewLoginPeriodIfNecessary();
    }

    public void addNewLoginPeriodIfNecessary() {
	if (getDateBeginContractYearMonthDay() != null && getDateEndContractYearMonthDay() != null
		&& !getDateEndContractYearMonthDay().isBefore(new YearMonthDay())) {

	    GrantOwner grantOwner = getGrantContract().getGrantOwner();
	    Login login = grantOwner.getPerson().createLoginIdentificationAndUserIfNecessary();
	    if (!loginPeriodAlreadyExists(login)) {
		new LoginPeriod(getDateBeginContractYearMonthDay(), getDateEndContractYearMonthDay(), login);
	    }
	}
    }

    private boolean loginPeriodAlreadyExists(Login login) {
	List<LoginPeriod> loginPeriods = login.getLoginPeriods();
	for (LoginPeriod loginPeriod : loginPeriods) {
	    if (loginPeriod.getBeginDate().equals(getDateBeginContractYearMonthDay()) && loginPeriod.getEndDate() != null
		    && loginPeriod.getEndDate().equals(getDateEndContractYearMonthDay())) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void setDateBeginContractYearMonthDay(YearMonthDay dateBeginContractYearMonthDay) {
	throw new DomainException("error.GrantContractRegime.impossible.edit.beginDate");
    }

    @Override
    public void setDateEndContractYearMonthDay(YearMonthDay dateEndContractYearMonthDay) {
	throw new DomainException("error.GrantContractRegime.impossible.edit.endDate");
    }

    @Override
    public void setDateBeginContract(Date date) {
	throw new DomainException("error.GrantContractRegime.impossible.edit.beginDate");
    }

    @Override
    public void setDateEndContract(Date date) {
	throw new DomainException("error.GrantContractRegime.impossible.edit.endDate");
    }

    public GrantSubsidy getGrantSubsidy() {
	for (GrantSubsidy grantSubsidy : getGrantContract().getAssociatedGrantSubsidies()) {
	    Interval interval = new Interval(getDateBeginContractYearMonthDay().toDateTimeAtMidnight(),
		    getDateEndContractYearMonthDay().toDateTimeAtMidnight().plus(1));
	    if (grantSubsidy.getDateEndSubsidyYearMonthDay() != null) {
		Interval grantSubsidyInterval = new Interval(grantSubsidy.getDateBeginSubsidyYearMonthDay()
			.toDateTimeAtMidnight(), grantSubsidy.getDateEndSubsidyYearMonthDay().toDateTimeAtMidnight());
		if (grantSubsidyInterval.overlaps(interval)) {
		    return grantSubsidy;
		}
	    } else if (grantSubsidy.getDateBeginSubsidyYearMonthDay() != null) {
		if (interval.contains(grantSubsidy.getDateBeginSubsidyYearMonthDay().toDateTimeAtMidnight())) {
		    return grantSubsidy;
		}
	    } else {
		return grantSubsidy;
	    }
	}
	return null;
    }
}
