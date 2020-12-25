package org.fenixedu.academic.domain.accounting;

import com.google.gson.JsonObject;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.util.Money;
import org.joda.time.LocalDate;

import java.util.Map;

public class CustomEvent extends CustomEvent_Base {

    public CustomEvent(final Person person, final Account account, final Map<LocalDate, Money> dueDateAmountMap,
                       final JsonObject config) {
        init(EventType.CUSTOM, person);
        setDueDateAmountMap(new DueDateAmountMap(dueDateAmountMap));
        setCustomToAccount(account);
        setConfig(config.toString());
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getExternalAccount();
    }

    @Override
    public Account getToAccount() {
        return getCustomToAccount();
    }

    @Override
    public PostingRule getPostingRule() {
        return CustomEventPostingRule.getInstance();
    }

    @Override
    public Unit getOwnerUnit() {
        return null;
    }

}
