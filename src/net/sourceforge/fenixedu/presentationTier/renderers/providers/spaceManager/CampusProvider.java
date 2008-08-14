package net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CampusProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	Set<Campus> result = new TreeSet<Campus>(Campus.COMPARATOR_BY_PRESENTATION_NAME);
	List<Campus> allActiveCampus = Space.getAllActiveCampus();
	result.addAll(allActiveCampus);
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
