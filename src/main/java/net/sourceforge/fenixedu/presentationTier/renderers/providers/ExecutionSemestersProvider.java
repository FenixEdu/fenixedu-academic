package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionSemestersProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final List<ExecutionSemester> executionSemesters =
                new ArrayList<ExecutionSemester>(Bennu.getInstance().getExecutionPeriodsSet());

        Collections.sort(executionSemesters, new ReverseComparator());

        return executionSemesters;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
