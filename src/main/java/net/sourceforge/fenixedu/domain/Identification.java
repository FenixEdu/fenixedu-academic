package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

/**
 * @author mrsp
 */

public abstract class Identification extends Identification_Base {

    protected Identification() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        super.setUser(null);
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public boolean isLogin() {
        return false;
    }

    @Override
    public void setUser(User user) {
        if (user == null) {
            throw new DomainException("error.identification.empty.user");
        }
        super.setUser(user);
    }

    public boolean belongsToPeriod(DateTime begin, DateTime end) {
        return ((end == null || !getBeginDateDateTime().isAfter(end)) && (getEndDateDateTime() == null || !getEndDateDateTime()
                .isBefore(begin)));
    }

    public boolean isActive(DateTime currentDate) {
        return belongsToPeriod(currentDate, currentDate);
    }

    public static List<Login> readAllLogins() {
        List<Login> logins = new ArrayList<Login>();
        for (Identification identification : RootDomainObject.getInstance().getIdentifications()) {
            if (identification.isLogin()) {
                logins.add((Login) identification);
            }
        }
        return logins;
    }

    @Deprecated
    public java.util.Date getBeginDate() {
        org.joda.time.DateTime dt = getBeginDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setBeginDate(java.util.Date date) {
        if (date == null) {
            setBeginDateDateTime(null);
        } else {
            setBeginDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Date getEndDate() {
        org.joda.time.DateTime dt = getEndDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setEndDate(java.util.Date date) {
        if (date == null) {
            setEndDateDateTime(null);
        } else {
            setEndDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}
