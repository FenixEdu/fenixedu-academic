package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

public enum CurricularRuleLevel {

    ENROLMENT_WITH_RULES, 
    
    ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT, 
    
    ENROLMENT_NO_RULES;

    public String getName() {
	return name();
    }

    static public CurricularRuleLevel defaultLevel() {
	return ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT;
    }
}
