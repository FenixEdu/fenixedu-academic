package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.Department;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ActiveDepartmentsProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final List<Department> departments = new ArrayList<Department>();
        for (final Department department : Bennu.getInstance().getDepartmentsSet()) {
            if (department.getActive() != null && department.getActive().booleanValue()) {
                departments.add(department);
            }
        }
        Collections.sort(departments, Department.COMPARATOR_BY_NAME);
        return departments;
    }

}
