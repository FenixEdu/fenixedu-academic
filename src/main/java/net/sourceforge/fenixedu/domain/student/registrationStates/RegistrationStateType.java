/**
 * 
 */
package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum RegistrationStateType {

    REGISTERED(true, true),

    MOBILITY(true, true),

    CANCELED(false, false),

    CONCLUDED(false, true),

    FLUNKED(false, false),

    INTERRUPTED(false, false),

    SCHOOLPARTCONCLUDED(true, true),

    INTERNAL_ABANDON(false, false),

    EXTERNAL_ABANDON(false, false),

    TRANSITION(false, true),

    TRANSITED(false, true),

    STUDYPLANCONCLUDED(false, true),

    INACTIVE(false, false); //Closed state for the registrations regarding the AFA & MA protocols

    private RegistrationStateType(final boolean active, final boolean canHaveCurriculumLinesOnCreation) {
        this.active = active;
        this.canHaveCurriculumLinesOnCreation = canHaveCurriculumLinesOnCreation;
    }

    private boolean active;

    private boolean canHaveCurriculumLinesOnCreation;

    public String getName() {
        return name();
    }

    public boolean isActive() {
        return active;
    }

    public boolean isInactive() {
        return !active;
    }

    public boolean canHaveCurriculumLinesOnCreation() {
        return canHaveCurriculumLinesOnCreation;
    }

    public String getQualifiedName() {
        return RegistrationStateType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return RegistrationStateType.class.getName() + "." + name();
    }

    public String getDescription() {
        return ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale()).getString(getQualifiedName());
    }

    public boolean canReingress() {
        return this == FLUNKED || this == INTERRUPTED || this == INTERNAL_ABANDON || this == EXTERNAL_ABANDON || this == CANCELED
                || this == INACTIVE;
    }

}
