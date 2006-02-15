/*
 * Created on Feb 2, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;


public enum CurricularRuleType {
    
    PRECEDENCY_APPROVED_DEGREE_MODULE,
    
    PRECEDENCY_ENROLED_DEGREE_MODULE,
    
    CREDITS_LIMIT,
    
    DEGREE_MODULES_SELECTION_LIMIT,
    
    ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR;

    public String getName() {
        return name();
    }
}
