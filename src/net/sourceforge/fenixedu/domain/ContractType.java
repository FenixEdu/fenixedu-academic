package net.sourceforge.fenixedu.domain;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum ContractType {

    EFFECTIVE, ON_TERM, INDEPENDENT_WORKER, INDEPENDENT_WORKER_WITH_EMPLOYEES, INDEPENDENT_WORKER_WITHOUT_EMPLOYEES,
    RECEIPT_CONTRACT, PROFESSIONAL_INTERNSHIP, SCHOLARSHIP, OTHER, NO_ANSWER;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return ContractType.class.getSimpleName() + "." + name();
    }

    public String getLocalizedName() {
        return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

}
