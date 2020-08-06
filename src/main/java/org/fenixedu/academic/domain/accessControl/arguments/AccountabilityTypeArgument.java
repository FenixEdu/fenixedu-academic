package org.fenixedu.academic.domain.accessControl.arguments;

import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class AccountabilityTypeArgument extends DomainObjectArgumentParser<AccountabilityType> {
    @Override
    public Class<AccountabilityType> type() {
        return AccountabilityType.class;
    }
}
