package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum ErasmusApplyForSemesterType implements IPresentableEnum {
    FIRST_SEMESTER, SECOND_SEMESTER;

    protected String localizedName(Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(fullQualifiedName());
    }

    protected String qualifiedName() {
        return this.getClass().getSimpleName() + "." + name();
    }

    protected String fullQualifiedName() {
        return this.getClass().getName() + "." + name();
    }

    protected String localizedName() {
        return localizedName(Language.getLocale());
    }

    @Override
    public String getLocalizedName() {
        return localizedName();
    }

}
