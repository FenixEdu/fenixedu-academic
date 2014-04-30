package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.space.Campus;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;
import org.fenixedu.bennu.core.groups.ArgumentParser;

@GroupArgumentParser
public class CampusArgument implements ArgumentParser<Campus> {
    @Override
    public Campus parse(String argument) {
        return Campus.readActiveCampusByName(argument);
    }

    @Override
    public String serialize(Campus argument) {
        return argument.getName();
    }

    @Override
    public Class<Campus> type() {
        return Campus.class;
    }
}
