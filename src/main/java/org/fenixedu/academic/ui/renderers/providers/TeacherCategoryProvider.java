package org.fenixedu.academic.ui.renderers.providers;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TeacherCategoryProvider implements DataProvider {
    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
        return Bennu.getInstance().getTeacherCategorySet();
    }
}
