package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PermissionType {

    MANAGE_CONCLUSION() {

	@Override
	protected Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    Set<AdministrativeOfficeType> administrativeOfficeTypeSet = new HashSet<AdministrativeOfficeType>();
	    administrativeOfficeTypeSet.add(AdministrativeOfficeType.DEGREE);
	    return administrativeOfficeTypeSet;
	}

    },

    UPDATE_REGISTRATION_WITH_CONCLUSION() {

	@Override
	protected Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    Set<AdministrativeOfficeType> administrativeOfficeTypeSet = new HashSet<AdministrativeOfficeType>();
	    administrativeOfficeTypeSet.add(AdministrativeOfficeType.DEGREE);
	    return administrativeOfficeTypeSet;
	}

    };
    
    private static final List<PermissionType> administratriveOfficePermissions = Arrays.asList(MANAGE_CONCLUSION, UPDATE_REGISTRATION_WITH_CONCLUSION);
    
    
    abstract protected Set<AdministrativeOfficeType> administrativeOfficeTypeContexts();

    protected String localizedName(Locale locale) {
	return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(qualifiedName());
    }

    protected String qualifiedName() {
	return StringAppender.append(PermissionType.class.getSimpleName(), ".", name());
    }

    protected String localizedName() {
	return localizedName(Language.getLocale());
    }

    public String getLocalizedName() {
	return localizedName();
    }
    
    public String getName() {
	return name();
    }
    
    public static List<PermissionType> getAdministrativeOfficePermissionTypes() {
	return administratriveOfficePermissions;
    }
    
}
