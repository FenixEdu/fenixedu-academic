package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.stm.Transaction;

import org.testng.annotations.Configuration;
import org.testng.annotations.Test;


public class TestNGBase {
    
    @Configuration(beforeTestClass=true)
    protected void setUp() throws Exception {        
        try {
            Transaction.abort();
            Transaction.commit();
        }catch (Exception e) {
            
        }               
        Transaction.begin();        
        DomainObject.turnOffLockMode();
        RootDomainObject.initTests();
    }
    
    @Configuration(afterTestClass=true)
    protected void tearDown() throws Exception {
        Transaction.abort();
    }


}
