package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EmailSenderProvider implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {
        return Sender.getAvailableSenders();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
