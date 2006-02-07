/*
 * Created on Feb 2, 2006
 */
package net.sourceforge.fenixedu.domain.curricularRules;


public enum CurricularRuleType {
    
    CREDITS_LIMIT,
    DEGREE_MODULES_SELECTION_LIMIT,
    PRECEDENCY_APPROVED_DEGREE_MODULE,
    PRECEDENCY_ENROLED_DEGREE_MODULE;

    public String getName() {
        return name();
    }
}
