package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;

public enum EnrolmentResultType {
    TEMPORARY(0, EnrollmentCondition.TEMPORARY),

    VALIDATED(1, EnrollmentCondition.VALIDATED),
    
    IMPOSSIBLE(2, EnrollmentCondition.IMPOSSIBLE),

    NULL(3, EnrollmentCondition.INVISIBLE);

    static private final EnrolmentResultType[][] AND_TABLE = new EnrolmentResultType[][] {
	    { TEMPORARY, TEMPORARY, IMPOSSIBLE, TEMPORARY },

	    { TEMPORARY, VALIDATED, IMPOSSIBLE, VALIDATED },

	    { IMPOSSIBLE, IMPOSSIBLE, IMPOSSIBLE, IMPOSSIBLE }, 
	    
	    { TEMPORARY, VALIDATED, IMPOSSIBLE, NULL }

    };

    static private final EnrolmentResultType[][] OR_TABLE = new EnrolmentResultType[][] {
	    { TEMPORARY, VALIDATED, TEMPORARY, TEMPORARY },

	    { VALIDATED, VALIDATED, VALIDATED, VALIDATED },
	    
	    { TEMPORARY, VALIDATED, IMPOSSIBLE, IMPOSSIBLE, },

	    { TEMPORARY, VALIDATED, IMPOSSIBLE, NULL }

    };

    private int order;

    private EnrollmentCondition enrollmentCondition;

    private EnrolmentResultType(int order, EnrollmentCondition enrollmentCondition) {
	this.order = order;
	this.enrollmentCondition = enrollmentCondition;
    }

    public int order() {
	return this.order;
    }

    public EnrollmentCondition getEnrollmentCondition() {
	return enrollmentCondition;
    }

    public String value() {
	return name();
    }

    public EnrolmentResultType and(final EnrolmentResultType enrolmentResultType) {
	return AND_TABLE[this.order][enrolmentResultType.order()];
    }

    public EnrolmentResultType or(final EnrolmentResultType enrolmentResultType) {
	return OR_TABLE[this.order][enrolmentResultType.order()];
    }

}
