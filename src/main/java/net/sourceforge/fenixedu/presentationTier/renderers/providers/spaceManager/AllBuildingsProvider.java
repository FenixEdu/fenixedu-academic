package net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager;

import java.util.stream.Collectors;

import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AllBuildingsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return Space.getSpaces().filter(s -> s.getClassification().equals(SpaceClassification.getByName("Building"))).collect(Collectors.toList());
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
