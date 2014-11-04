package net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.spaceManager.FindSpacesBean;

import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class BuildingProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        Set<Space> result = new HashSet<Space>();
        FindSpacesBean bean = (FindSpacesBean) source;
        Space campus = bean.getCampus();

        SpaceClassification building = SpaceClassification.getByName("Building");

        if (campus != null) {
            for (Space child : campus.getChildren()) {
                if (child.getClassification().equals(building)) {
                    result.add(child);
                }
            }
        }

        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
