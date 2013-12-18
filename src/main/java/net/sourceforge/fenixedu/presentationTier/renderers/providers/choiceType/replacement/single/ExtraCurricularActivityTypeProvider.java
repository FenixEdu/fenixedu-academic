package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single;

import java.util.ArrayList;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExtraCurricularActivityTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return new ArrayList(Bennu.getInstance().getExtraCurricularActivityTypeSet());
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
