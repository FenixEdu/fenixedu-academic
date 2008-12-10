package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

public class QueueJobContext extends QueueJobContext_Base {
    
    public  QueueJobContext() {
        super();
        this.setRequestDate(new DateTime());
    }
    
}
