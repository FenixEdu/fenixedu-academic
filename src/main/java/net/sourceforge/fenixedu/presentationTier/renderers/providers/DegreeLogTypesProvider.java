package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.dataTransferObject.coordinator.SearchDegreeLogBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeLogTypesProvider implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final SearchDegreeLogBean searchDegreeLogBean = (SearchDegreeLogBean) source;
        return searchDegreeLogBean.getDegreeLogTypesAll();
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
