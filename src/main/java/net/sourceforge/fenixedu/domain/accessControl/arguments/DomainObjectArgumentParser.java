package net.sourceforge.fenixedu.domain.accessControl.arguments;

import org.fenixedu.bennu.core.groups.ArgumentParser;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

public abstract class DomainObjectArgumentParser<T extends DomainObject> implements ArgumentParser<T> {
    @Override
    public T parse(String argument) {
        return FenixFramework.getDomainObject(argument);
    }

    @Override
    public String serialize(T argument) {
        return argument.getExternalId();
    }
}
