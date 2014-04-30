package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class FunctionTypeArgument extends EnumArgument<FunctionType> {
    @Override
    public Class<FunctionType> type() {
        return FunctionType.class;
    }
}
