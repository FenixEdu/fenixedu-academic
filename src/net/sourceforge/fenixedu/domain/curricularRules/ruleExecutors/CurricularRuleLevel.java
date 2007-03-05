package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

public enum CurricularRuleLevel {

    ENROLMENT_WITH_RULES, 
    
    ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT, 
    
    ENROLMENT_NO_RULES,
    
    IMPROVEMENT_ENROLMENT,
    
    SPECIAL_SEASON_ENROLMENT;

    public String getName() {
	return name();
    }

    static public CurricularRuleLevel defaultLevel() {
	return ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT;
    }

    public boolean managesEnrolments() {
	switch (this) {
	case IMPROVEMENT_ENROLMENT:
	    return false;
	case SPECIAL_SEASON_ENROLMENT:
	    return false;
	default:
	    return true;
	}
    }

}
