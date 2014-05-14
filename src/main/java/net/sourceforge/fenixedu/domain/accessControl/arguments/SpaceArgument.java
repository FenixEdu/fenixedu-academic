package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.space.SpaceUtils;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;
import org.fenixedu.bennu.core.groups.ArgumentParser;
import org.fenixedu.spaces.domain.Space;

@GroupArgumentParser
public class SpaceArgument implements ArgumentParser<Space> {
    @Override
    public Space parse(String argument) {
        return SpaceUtils.getSpaceByName(argument);
    }

    @Override
    public String serialize(Space argument) {
        return argument.getName();
    }

    @Override
    public Class<Space> type() {
        return Space.class;
    }
}
