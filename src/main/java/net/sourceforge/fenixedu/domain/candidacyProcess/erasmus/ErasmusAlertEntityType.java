package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Locale;
import java.util.ResourceBundle;

public enum ErasmusAlertEntityType {
    GRI, ACADEMIC_OFFICE, COORDINATOR;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return ErasmusAlertEntityType.class.getSimpleName() + "." + getName();
    }

    public String getFullyQualifiedName() {
        return ErasmusAlertEntityType.class.getName() + "." + getName();
    }

    public String getDescription() {
        return getDescription(Locale.getDefault());
    }

    public String getDescription(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getFullyQualifiedName());
    }
}
