package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class ThesisArgument extends DomainObjectArgumentParser<Thesis> {
    @Override
    public Class<Thesis> type() {
        return Thesis.class;
    }
}
