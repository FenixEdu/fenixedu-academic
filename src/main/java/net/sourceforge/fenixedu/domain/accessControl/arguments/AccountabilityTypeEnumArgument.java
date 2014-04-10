package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class AccountabilityTypeEnumArgument extends EnumArgument<AccountabilityTypeEnum> {
    @Override
    public Class<AccountabilityTypeEnum> type() {
        return AccountabilityTypeEnum.class;
    }
}
