package net.sourceforge.fenixedu.domain.thesis;

import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

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
        return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(name());
    }
}
