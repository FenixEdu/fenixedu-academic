package org.fenixedu.academic.ui.renderers.legacy;

import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixWebFramework.rendererExtensions.ShortLocalizedStringRenderer;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ShortMultiLanguageStringRenderer extends ShortLocalizedStringRenderer {

    @Override
    protected LocalizedString getLocalized(Object object) {
        if (object instanceof MultiLanguageString) {
            return ((MultiLanguageString) object).toLocalizedString();
        } else {
            return super.getLocalized(object);
        }
    }

}
