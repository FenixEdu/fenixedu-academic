package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.UnitSite;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class UnitSiteArgument extends DomainObjectArgumentParser<UnitSite> {
    @Override
    public Class<UnitSite> type() {
        return UnitSite.class;
    }
}
