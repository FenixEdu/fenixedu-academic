package net.sourceforge.fenixedu.presentationTier.renderers.legacy;

import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixWebFramework.rendererExtensions.LocalizedStringInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MultiLanguageStringInputRenderer extends LocalizedStringInputRenderer {

    @Override
    protected Converter getConverter() {
        return new MultiLanguageStringConverter();
    }

    @Override
    protected LocalizedString getLocalized(Object object) {
        if (object instanceof MultiLanguageString) {
            return ((MultiLanguageString) object).toLocalizedString();
        } else {
            return super.getLocalized(object);
        }
    }

    public static class MultiLanguageStringConverter extends LocalizedStringConverter {
        @Override
        protected Object processLocalized(LocalizedString mls) {
            return MultiLanguageString.fromLocalizedString(mls);
        }
    }

}
