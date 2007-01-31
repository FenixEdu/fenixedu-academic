package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class RuleResult {

    private RuleResultType result;

    private LabelFormatter message;

    private RuleResult(final RuleResultType result) {
	this.result = result;
    }

    private RuleResult(final RuleResultType result, final LabelFormatter message) {
	this(result);
	this.message = message;
    }

    private RuleResultType getResult() {
	return this.result;
    }

    public LabelFormatter getMessage() {
	return message;
    }

    public boolean hasMessage() {
	return this.message != null && !this.message.isEmpty();
    }

    public RuleResult and(RuleResult ruleResult) {
	return new RuleResult(AND_TABLE[this.getResult().order()][ruleResult.getResult().order()]);
    }

    public RuleResult or(RuleResult ruleResult) {
	return new RuleResult(OR_TABLE[this.getResult().order()][ruleResult.getResult().order()]);
    }

    public RuleResult not() {
	return new RuleResult(NOT_TABLE[this.getResult().order()]);
    }

    public boolean isTrue() {
	return getResult() == RuleResultType.TRUE;
    }

    public boolean isFalse() {
	return getResult() == RuleResultType.FALSE;
    }

    public boolean isNA() {
	return getResult() == RuleResultType.NA;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj instanceof RuleResult) { return this.result == ((RuleResult) obj).getResult(); }
        return false;
    }

    private enum RuleResultType {
	FALSE(0), TRUE(1), NA(2);

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
    }

    static public RuleResult createTrue() {
	return new RuleResult(RuleResultType.TRUE);
    }
    
    static public RuleResult createFalse() {
	return new RuleResult(RuleResultType.FALSE);
    }

    static public RuleResult createFalse(final LabelFormatter message) {
	return new RuleResult(RuleResultType.FALSE, message);
    }

    static public RuleResult createNA() {
	return new RuleResult(RuleResultType.NA);
    }

    static private final RuleResultType[][] AND_TABLE = new RuleResultType[][] {
	    { RuleResultType.FALSE, RuleResultType.FALSE, RuleResultType.FALSE },
	    { RuleResultType.FALSE, RuleResultType.TRUE, RuleResultType.TRUE },
	    { RuleResultType.FALSE, RuleResultType.TRUE, RuleResultType.NA } };

    static private final RuleResultType[][] OR_TABLE = new RuleResultType[][] {
	    { RuleResultType.FALSE, RuleResultType.TRUE, RuleResultType.FALSE },
	    { RuleResultType.TRUE, RuleResultType.TRUE, RuleResultType.TRUE },
	    { RuleResultType.FALSE, RuleResultType.TRUE, RuleResultType.NA } };

    static private final RuleResultType[] NOT_TABLE = new RuleResultType[] {
	RuleResultType.TRUE, RuleResultType.FALSE, RuleResultType.NA };
}
