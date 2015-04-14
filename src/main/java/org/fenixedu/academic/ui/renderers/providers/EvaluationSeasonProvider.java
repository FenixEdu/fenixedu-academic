package org.fenixedu.academic.ui.renderers.providers;

import java.util.stream.Collectors;

import org.fenixedu.academic.domain.EvaluationSeason;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EvaluationSeasonProvider implements DataProvider {
    @Override
    public Object provide(Object source, Object currentValue) {
        return EvaluationSeason.all().sorted().collect(Collectors.toList());
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}