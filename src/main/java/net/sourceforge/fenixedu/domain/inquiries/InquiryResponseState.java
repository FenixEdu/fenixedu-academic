/**
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import java.util.Locale;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public enum InquiryResponseState implements IPresentableEnum {

    COMPLETE, PARTIALLY_FILLED, INCOMPLETE, EMPTY;

    @Override
    public String getLocalizedName() {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale());
        return bundle.getString(this.getClass().getName() + "." + name());
    }
}
