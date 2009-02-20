/**
 * 
 */
package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum RegistrationStateType {

    REGISTERED(true, false, true),

    MOBILITY(true, false, true),

    CANCELED(false, true, false),

    CONCLUDED(false, false, true),

    FLUNKED(false, true, false),

    INTERRUPTED(false, true, false),

    SCHOOLPARTCONCLUDED(true, false, true),

    INTERNAL_ABANDON(false, true, false),

    EXTERNAL_ABANDON(false, true, false),

    TRANSITION(false, false, true),

    TRANSITED(false, false, true),

    STUDYPLANCONCLUDED(false, false, true);

    private RegistrationStateType(final boolean active, final boolean deleteActualPeriodInfo,
	    final boolean canHaveCurriculumLinesOnCreation) {
	this.active = active;
	this.deleteActualPeriodInfo = deleteActualPeriodInfo;
	this.canHaveCurriculumLinesOnCreation = canHaveCurriculumLinesOnCreation;
    }

    private boolean active;

    private boolean deleteActualPeriodInfo;

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

    public boolean deleteActualPeriodInfo() {
	return deleteActualPeriodInfo;
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
	return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(getQualifiedName());
    }

    public boolean canReingress() {
	return this == FLUNKED || this == INTERRUPTED || this == INTERNAL_ABANDON || this == EXTERNAL_ABANDON || this == CANCELED;
    }

}
