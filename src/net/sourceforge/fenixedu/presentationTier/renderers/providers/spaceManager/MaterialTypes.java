package net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.material.Extension;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class MaterialTypes implements DataProvider {

    public Object provide(Object source, Object currentValue) {        
        Set<Class> materialTypes = new HashSet<Class>();        
        materialTypes.add(Extension.class);
        return materialTypes;
    }

    public Converter getConverter() {
        return null;        
    }
}
