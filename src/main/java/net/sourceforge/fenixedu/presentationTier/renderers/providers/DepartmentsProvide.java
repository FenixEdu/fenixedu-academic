package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.Department;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DepartmentsProvide implements DataProvider {
    @Override
    public Object provide(Object source, Object currentValue) {

        final List<Department> departments = new ArrayList<Department>(Bennu.getInstance().getDepartmentsSet());

        return departments;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
