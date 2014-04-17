package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import java.util.Locale;

/**
 * The {@link AdministrativeOfficeType} was used as a selector for the {@link AdministrativeOffice} from a given context (e.g. a
 * Degree, an
 * AcademicServiceRequest, etc). Offices now have a direct link for the {@link AcademicProgram}s they manage, rendering this class
 * useless. To select
 * the office of your context you must traverse to the associated {@link AcademicProgram}.
 */
@Deprecated
public enum AdministrativeOfficeType {

    DEGREE, MASTER_DEGREE;

    @Deprecated
    public String getName() {
        return name();
    }

    @Deprecated
    public String getQualifiedName() {
        return AdministrativeOfficeType.class.getSimpleName() + "." + name();
    }

    @Deprecated
    public String getFullyQualifiedName() {
        return AdministrativeOfficeType.class.getName() + "." + name();
    }

    @Deprecated
    public String getDescription() {
        return ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale()).getString(getQualifiedName());
    }

    @Deprecated
    public AdministrativeOffice getAdministrativeOffice() {
        return AdministrativeOffice.readByAdministrativeOfficeType(this);
    }

}
