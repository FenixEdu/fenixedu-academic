package net.sourceforge.fenixedu.domain.protocols;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class ProtocolHistory extends ProtocolHistory_Base {
    
    public ProtocolHistory(YearMonthDay beginDate, YearMonthDay endDate) {
        super();        
        if(endDate != null && beginDate.isAfter(endDate)){
            throw new DomainException("error.protocols.beginDateBiggerThenEnd");
        }
        setRootDomainObject(RootDomainObject.getInstance());
        setBeginDate(beginDate);
        setEndDate(endDate);
    }
    
}
