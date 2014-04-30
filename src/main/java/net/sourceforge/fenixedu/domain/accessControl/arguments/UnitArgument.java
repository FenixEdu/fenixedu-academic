package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class UnitArgument extends DomainObjectArgumentParser<Unit> {
    @Override
    public Class<Unit> type() {
        return Unit.class;
    }
}
