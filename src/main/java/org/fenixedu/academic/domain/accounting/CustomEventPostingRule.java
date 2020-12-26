package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.Singleton;
import org.joda.time.DateTime;

import java.util.function.Supplier;

public class CustomEventPostingRule extends CustomEventPostingRule_Base {

    private static final Supplier<CustomEventPostingRule> GETTER = () -> Bennu.getInstance().getPostingRulesSet().stream()
            .filter(CustomEventPostingRule.class::isInstance)
            .map(CustomEventPostingRule.class::cast)
            .findAny().orElse(null);

    private CustomEventPostingRule() {
        super();
        setRootDomainObject(Bennu.getInstance());
//        setStartDate(null);
//        setEndDate(null);
//        setEventType(EventType.CUSTOM);
//        setServiceAgreementTemplate(null);
        final Unit unit = Bennu.getInstance().getInstitutionUnit();
        init(EventType.CUSTOM, new DateTime(), null, unit.getUnitServiceAgreementTemplate());
    }

    public static CustomEventPostingRule getInstance() {
        return Singleton.getInstance(GETTER, () -> new CustomEventPostingRule());
    }

    @Override
    protected Money doCalculationForAmountToPay(final Event event) {
        return event.getAmountToPay();
    }

    @Override
    protected EntryType getEntryType() {
        return EntryType.CUSTOM;
    }

}
