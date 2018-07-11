package org.fenixedu.academic.domain.accounting.events.gratuity;

import org.fenixedu.academic.domain.accounting.PostingRule;

public class PartialRegimeEvent extends PartialRegimeEvent_Base {
    
    public PartialRegimeEvent() {
        super();
    }

    @Override
    public PostingRule getPostingRule() {
        return super.getEventPostingRule();
    }
}
