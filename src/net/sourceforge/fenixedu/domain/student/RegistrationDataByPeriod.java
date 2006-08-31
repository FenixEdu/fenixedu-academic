package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class RegistrationDataByPeriod extends RegistrationDataByPeriod_Base {
    
    public  RegistrationDataByPeriod() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setExecutionPeriod(ExecutionPeriod.readActualExecutionPeriod());
    }
    
}
