package net.sourceforge.fenixedu.renderers;

import java.util.EnumSet;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * The <code>EnumSetRenderer</code> provides a simple presentation for enumeration set values. An
 * enumSet value will be displayed in one of two forms. First a bundle named
 * <code>ENUMERATION_RESOURCES</code> is used. Using as key the enum name a localized message is
 * searched. If the bundle is not defined or the key does not exist in the bundle then the programmatic
 * name of the enum is presented.
 * 
 */
public class EnumSetRenderer extends OutputRenderer {

    // NOTE: duplicate code with EnumInputRenderer
    protected String getEnumSetDescription(EnumSet enumset) {

        Object[] enumSetArray = enumset.toArray();
        StringBuffer description = new StringBuffer();

        for (Object enumSetObject : enumSetArray) {

            String thisDescription = RenderUtils.getResourceString("ENUMERATION_RESOURCES",
                    enumSetObject.toString());
            if (thisDescription == null) {
                thisDescription = RenderUtils.getResourceString(enumSetObject.toString());
            }

            if (description.length() != 0) {
                description.append(", ");
            }
            description.append(thisDescription);
        }
        return description.toString();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                EnumSet enumSet = (EnumSet) object;

                if (enumSet == null) {
                    return new HtmlText();
                }

                String description = getEnumSetDescription(enumSet);

                return new HtmlText(description);
            }

        };
    }
}
