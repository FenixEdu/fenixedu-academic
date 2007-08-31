/**
 * 
 */
package net.sourceforge.fenixedu.domain.curricularRules.executors;

public enum RuleResultType {
    FALSE(0), TRUE(1), NA(2), WARNING(3);

    static private final RuleResultType[][] AND_TABLE = new RuleResultType[][] {

    { FALSE, FALSE, FALSE, FALSE },

    { FALSE, TRUE, TRUE, WARNING },

    { FALSE, TRUE, NA, WARNING },

    { FALSE, WARNING, WARNING, WARNING }

    };

    static private final RuleResultType[][] OR_TABLE = new RuleResultType[][] {

    { FALSE, TRUE, FALSE, WARNING },

    { TRUE, TRUE, TRUE, WARNING },

    { FALSE, TRUE, NA, WARNING },

    { WARNING, WARNING, WARNING, WARNING }

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