package net.sourceforge.fenixedu.presentationTier.renderers.legacy;

import net.sourceforge.fenixedu.presentationTier.renderers.legacy.MultiLanguageStringInputRenderer.MultiLanguageStringConverter;

import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixWebFramework.rendererExtensions.LocalizedRichTextInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MultiLanguageRichTextInputRenderer extends LocalizedRichTextInputRenderer {

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

}
