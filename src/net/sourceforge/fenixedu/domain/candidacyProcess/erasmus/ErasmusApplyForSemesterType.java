package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum ErasmusApplyForSemesterType {
    FIRST_SEMESTER, SECOND_SEMESTER;

    protected String localizedName(Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(fullQualifiedName());
    }

    protected String qualifiedName() {
        return StringAppender.append(this.getClass().getSimpleName(), ".", name());
    }

    protected String fullQualifiedName() {
        return StringAppender.append(this.getClass().getName(), ".", name());
    }

    protected String localizedName() {
        return localizedName(Language.getLocale());
    }

    public String getLocalizedName() {
        return localizedName();
    }

}
