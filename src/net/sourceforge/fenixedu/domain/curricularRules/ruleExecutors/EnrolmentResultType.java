package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

public enum EnrolmentResultType {
    TEMPORARY(0), FINAL(1), NULL(2);

    static private final EnrolmentResultType[][] AND_TABLE = new EnrolmentResultType[][] {
	    { TEMPORARY, TEMPORARY, TEMPORARY },

	    { TEMPORARY, FINAL, FINAL },

	    { TEMPORARY, FINAL, NULL }

    };

    static private final EnrolmentResultType[][] OR_TABLE = new EnrolmentResultType[][] {
	    { TEMPORARY, FINAL, TEMPORARY },

	    { FINAL, FINAL, FINAL },

	    { TEMPORARY, FINAL, NULL }

    };

    private int order;

    private EnrolmentResultType(int order) {
	this.order = order;
    }

    public int order() {
	return this.order;
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
