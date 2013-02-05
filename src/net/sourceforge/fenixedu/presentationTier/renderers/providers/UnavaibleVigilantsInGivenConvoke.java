package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class UnavaibleVigilantsInGivenConvoke implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        ConvokeBean bean = (ConvokeBean) source;
        return bean.getUnavailableVigilants();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}
