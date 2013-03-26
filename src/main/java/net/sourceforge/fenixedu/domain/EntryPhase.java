package net.sourceforge.fenixedu.domain;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum EntryPhase {

    FIRST_PHASE(1),

    SECOND_PHASE(2),

    THIRD_PHASE(3);

    private int phaseNumber;

    private EntryPhase(int phaseNumber) {
        this.phaseNumber = phaseNumber;
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return getClass().getName() + "." + name();
    }

    public String getLocalizedName() {
        return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }

    static public EntryPhase valueOf(int phaseNumber) {
        for (final EntryPhase phase : values()) {
            if (phase.phaseNumber == phaseNumber) {
                return phase;
            }
        }
        return null;
    }

}
