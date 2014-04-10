package net.sourceforge.fenixedu.domain.accessControl.arguments;

import org.fenixedu.bennu.core.groups.ArgumentParser;

public abstract class EnumArgument<T extends Enum<T>> implements ArgumentParser<T> {

    @Override
    public T parse(String argument) {
        return Enum.valueOf(type(), argument);
    }

    @Override
    public String serialize(T argument) {
        return argument.name();
    }
}
