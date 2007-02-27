/**
 * 
 */
package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

public enum RuleResultType {
    FALSE(0, true), TRUE(1), NA(2), WARNING(3, true);

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

    private boolean copyMessages;

    private RuleResultType(int order) {
	this(order, false);
    }

    private RuleResultType(int order, boolean copyMessages) {
	this.order = order;
	this.copyMessages = copyMessages;
    }

    public int order() {
	return this.order;
    }

    public String value() {
	return name();
    }

    public boolean isToCopyMessages() {
	return copyMessages;
    }

    public RuleResultType and(final RuleResultType ruleResultType) {
	return AND_TABLE[this.order][ruleResultType.order()];
    }

    public RuleResultType or(final RuleResultType ruleResultType) {
	return OR_TABLE[this.order][ruleResultType.order()];
    }

}