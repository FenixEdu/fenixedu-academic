package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DepartmentsProvide implements DataProvider{
    public Object provide(Object source, Object currentValue) {

	final List<Department> departments = new ArrayList<Department>(RootDomainObject.getInstance()
		.getDepartments());

	return departments;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
