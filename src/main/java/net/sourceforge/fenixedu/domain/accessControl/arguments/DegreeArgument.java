package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.Degree;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;
import org.fenixedu.bennu.core.groups.ArgumentParser;

@GroupArgumentParser
public class DegreeArgument implements ArgumentParser<Degree> {
    @Override
    public Degree parse(String argument) {
        return Degree.readBySigla(argument);
    }

    @Override
    public String serialize(Degree argument) {
        return argument.getSigla();
    }

    @Override
    public Class<Degree> type() {
        return Degree.class;
    }
}
