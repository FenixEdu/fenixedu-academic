package net.sourceforge.fenixedu.domain.thesis;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import java.util.Locale;

/**
 * The visibility of all files submitted by the student for a thesis. The thesis
 * visibility is decided in a declaration that the student must accept before
 * submitting any file.
 * 
 * @author cfgi
 */
public enum ThesisVisibilityType implements IPresentableEnum {
    PUBLIC, INTRANET;

    @Override
    public String getLocalizedName() {
        return ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale()).getString(name());
    }
}
