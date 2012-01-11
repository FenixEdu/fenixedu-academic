package net.sourceforge.fenixedu.domain;

import java.util.ResourceBundle;

public enum ProfessionType {

    UNKNOWN(false),

    PUBLIC_ADMINISTRATION_BOARD_OR_DIRECTOR_AND_BOARD_OF_COMPANIES(true),

    CIENTIFIC_AND_INTELECTUAL_PROFESSION_SPECIALIST(true),

    INTERMEDIATE_LEVEL_TECHNICALS_AND_PROFESSIONALS(true),

    ADMINISTRATIVE_STAFF_AND_SIMMILAR(true),

    SALES_AND_SERVICE_STAFF(true),

    FARMERS_AND_AGRICULTURE_AND_FISHING_QUALIFIED_WORKERS(true),

    WORKERS_CRAFTSMEN_AND_SIMMILAR(true),

    INSTALLATION_AND_MACHINE_WORKERS_AND_LINE_ASSEMBLY_WORKERS(true),

    NON_QUALIFIED_WORKERS(true),

    MILITARY_MEMBER(true),

    OTHER(true);

    private boolean active;

    private ProfessionType(boolean active) {
	setActive(active);
    }

    public void setActive(boolean active) {
	this.active = active;
    }

    public boolean isActive() {
	return active;
    }

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return ProfessionType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return ProfessionType.class.getName() + "." + name();
    }

    public String getLocalizedName() {
	return ResourceBundle.getBundle("resources.EnumerationResources").getString(getQualifiedName());
    }

}
