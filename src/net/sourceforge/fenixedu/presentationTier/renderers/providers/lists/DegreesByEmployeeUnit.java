package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesByEmployeeUnit implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	if (AccessControl.getPerson().isAdministrativeOfficeEmployee()) {
	    return AccessControl.getPerson().getEmployee().getAdministrativeOffice().getAdministratedDegrees();
	}

	SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	result.addAll(RootDomainObject.getInstance().getDegrees());
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
