package net.sourceforge.fenixedu.domain.exceptions.accounting;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class PostingRuleDomainException extends DomainException {

    private LabelFormatter labelFormatterArgs;

    public PostingRuleDomainException(String key, String[] args) {
        super(key, args);
    }

    public PostingRuleDomainException(String key, LabelFormatter labelFormaterArgs) {
        super(key);
        this.labelFormatterArgs = labelFormaterArgs;
    }

    public LabelFormatter getLabelFormatterArgs() {
        return labelFormatterArgs;
    }

}
