/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.DateFormatUtil;



/**
 * @author jpvl
 */
public abstract class EnrolmentPeriod extends EnrolmentPeriod_Base {

    public EnrolmentPeriod() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public boolean isValid() {
    	Date date = new Date();
    	return isValid(date);
    }
    
    public boolean isValid(Date date) {
    	return (DateFormatUtil.compareDates("yyyyMMddHHmm", this.getStartDate(), date) <= 0) && (DateFormatUtil.compareDates("yyyyMMddHHmm", this.getEndDate(), date) >= 0);  
    }

}