package net.sourceforge.fenixedu.presentationTier.renderers.providers.pedagogicalCouncil;

import net.sourceforge.fenixedu.dataTransferObject.commons.delegates.DelegateSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.delegates.DelegateBean;
import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesGivenDegreeTypeForDelegatesManagement implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        if (source instanceof DelegateBean) {
            return Degree.readAllByDegreeType(((DelegateBean) source).getDegreeType());
        } else {
            return Degree.readAllByDegreeType(((DelegateSearchBean) source).getDegreeType());
        }

    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
