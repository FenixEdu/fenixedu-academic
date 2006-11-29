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
        setOjbConcreteClass(getClass().getName());
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
	if(user == null) {
	    throw new DomainException("error.identification.empty.user");
	}
	super.setUser(user);
    }

    public boolean belongsToPeriod(DateTime begin, DateTime end) {
	return ((end == null || !this.getBeginDateDateTime().isAfter(end)) && (this.getEndDateDateTime() == null || !this
		.getEndDateDateTime().isBefore(begin)));
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
}
