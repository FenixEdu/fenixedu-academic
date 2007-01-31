/**
 * 
 */
package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

public enum RuleResultType {
    FALSE(0), TRUE(1), NA(2);

    static private final RuleResultType[][] AND_TABLE = new RuleResultType[][] {

    { FALSE, FALSE, FALSE },

    { FALSE, TRUE, TRUE },

    { FALSE, TRUE, NA }

    };

    static private final RuleResultType[][] OR_TABLE = new RuleResultType[][] {

    { FALSE, TRUE, FALSE },

    { TRUE, TRUE, TRUE },

    { FALSE, TRUE, NA }

    };

    private int order;

    private RuleResultType(int order) {
	this.order = order;
    }

    public int order() {
	return this.order;
    }

    public String value() {
	return name();
    }

    public RuleResultType and(final RuleResultType ruleResultType) {
	return AND_TABLE[this.order][ruleResultType.order()];
    }

    public RuleResultType or(final RuleResultType ruleResultType) {
	return OR_TABLE[this.order][ruleResultType.order()];
    }

}