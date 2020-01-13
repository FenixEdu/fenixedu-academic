package org.fenixedu.academic.ui.renderers.providers;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class FirstExecutionSemestersProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return Bennu.getInstance().getExecutionPeriodsSet().stream().filter(es -> es.getChildOrder() == 1)
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
