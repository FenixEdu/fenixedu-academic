package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class VigilancyArgument extends DomainObjectArgumentParser<Vigilancy> {
    @Override
    public Class<Vigilancy> type() {
        return Vigilancy.class;
    }
}
