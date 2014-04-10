package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.Grouping;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class GroupingArgument extends DomainObjectArgumentParser<Grouping> {
    @Override
    public Class<Grouping> type() {
        return Grouping.class;
    }
}
