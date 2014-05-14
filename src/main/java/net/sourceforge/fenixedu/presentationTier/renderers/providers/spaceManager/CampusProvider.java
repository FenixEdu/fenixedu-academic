package net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager;

import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.spaces.domain.Space;

import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CampusProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        Set<Space> result = new TreeSet<Space>(SpaceUtils.COMPARATOR_BY_PRESENTATION_NAME);
        Set<Space> allActiveCampus = Space.getAllCampus();
        result.addAll(allActiveCampus);
        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
