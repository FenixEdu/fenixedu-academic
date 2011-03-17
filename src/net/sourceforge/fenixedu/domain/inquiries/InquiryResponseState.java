/**
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public enum InquiryResponseState implements IPresentableEnum {

    COMPLETE, PARTIALLY_FILLED, INCOMPLETE, EMPTY;

    @Override
    public String getLocalizedName() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	return bundle.getString(this.getClass().getName() + "." + name());
    }
}
