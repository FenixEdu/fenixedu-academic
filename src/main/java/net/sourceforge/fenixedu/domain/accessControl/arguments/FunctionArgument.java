package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.organizationalStructure.Function;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class FunctionArgument extends DomainObjectArgumentParser<Function> {
    @Override
    public Class<Function> type() {
        return Function.class;
    }
}
