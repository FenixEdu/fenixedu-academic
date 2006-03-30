package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantContractRegime extends GrantContractRegime_Base {

    public GrantContractRegime() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public Boolean getContractRegimeActive() {
        if (getDateEndContract().after(Calendar.getInstance().getTime())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        if (!this.getDateBeginContract().after(endDate)
                && !this.getDateEndContract().before(beginDate)) {
            return true;
        }
        return false;
    }

    public void delete() {
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
