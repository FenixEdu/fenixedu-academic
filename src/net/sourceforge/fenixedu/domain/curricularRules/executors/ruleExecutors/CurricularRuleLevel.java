package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

public enum CurricularRuleLevel {

    ENROLMENT_WITH_RULES(true),

    ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT(true),

    ENROLMENT_NO_RULES(true),

    ENROLMENT_VERIFICATION_WITH_RULES(true),

    IMPROVEMENT_ENROLMENT(false),

    SPECIAL_SEASON_ENROLMENT(false);

    private boolean isNormal;

    private CurricularRuleLevel(boolean isNormal) {
	this.isNormal = isNormal;
    }

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

    public boolean isNormal() {
	return isNormal;
    }

}
