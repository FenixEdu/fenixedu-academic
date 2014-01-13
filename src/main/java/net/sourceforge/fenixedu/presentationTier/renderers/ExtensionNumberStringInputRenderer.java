package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.material.Extension;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.ExtensionNumberConverter;
import pt.ist.fenixWebFramework.renderers.StringInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlFormComponent;

import com.google.common.base.Predicate;

public class ExtensionNumberStringInputRenderer extends StringInputRenderer {

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {

        Extension extension = (Extension) object;
        String number = (extension != null) ? extension.getIdentification().toString() : null;

        final HtmlComponent htmlComponent = super.createTextField(number, type);
        final HtmlFormComponent formComponent = (HtmlFormComponent) htmlComponent.getChild(new Predicate<HtmlComponent>() {
            @Override
            public boolean apply(HtmlComponent input) {
                return input instanceof HtmlFormComponent;
            }
        });

        formComponent.setConverter(new ExtensionNumberConverter());

        return formComponent;
    }
}
