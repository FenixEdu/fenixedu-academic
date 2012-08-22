package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PermissionType {

    MANAGE_CONCLUSION() {

	@Override
	public Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    return new HashSet<AdministrativeOfficeType>() {
		{
		    add(AdministrativeOfficeType.DEGREE);
		    add(AdministrativeOfficeType.MASTER_DEGREE);
		}
	    };
	}

    },

    UPDATE_REGISTRATION_AFTER_CONCLUSION() {

	@Override
	public Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    return new HashSet<AdministrativeOfficeType>() {
		{
		    add(AdministrativeOfficeType.DEGREE);
		    add(AdministrativeOfficeType.MASTER_DEGREE);
		}
	    };
	}

    },

    REPEAT_CONCLUSION_PROCESS() {

	@Override
	public Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    return new HashSet<AdministrativeOfficeType>() {
		{
		    add(AdministrativeOfficeType.DEGREE);
		    add(AdministrativeOfficeType.MASTER_DEGREE);
		}
	    };
	}
    },

    ENROLMENT_WITHOUT_RULES() {
	@Override
	public Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    return new HashSet<AdministrativeOfficeType>() {
		{
		    add(AdministrativeOfficeType.DEGREE);
		    add(AdministrativeOfficeType.MASTER_DEGREE);
		}
	    };
	}
    },

    MOVE_CURRICULUM_LINES_WITHOUT_RULES() {
	@Override
	public Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    return new HashSet<AdministrativeOfficeType>() {
		{
		    add(AdministrativeOfficeType.DEGREE);
		    add(AdministrativeOfficeType.MASTER_DEGREE);
		}
	    };
	}
    },

    MANAGE_MARKSHEETS() {
	@Override
	public Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    return new HashSet<AdministrativeOfficeType>() {
		{
		    add(AdministrativeOfficeType.DEGREE);
		    add(AdministrativeOfficeType.MASTER_DEGREE);
		}
	    };
	}
    },

    RECTIFICATION_MARKSHEETS() {

	@Override
	public Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    return new HashSet<AdministrativeOfficeType>() {
		{
		    add(AdministrativeOfficeType.DEGREE);
		    add(AdministrativeOfficeType.MASTER_DEGREE);
		}
	    };
	}
    },

    DISSERTATION_MARKSHEETS() {

	@Override
	public Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    return new HashSet<AdministrativeOfficeType>() {
		{
		    add(AdministrativeOfficeType.DEGREE);
		    add(AdministrativeOfficeType.MASTER_DEGREE);
		}
	    };
	}
    },

    REGISTRATION_CONCLUSION_CURRICULUM_VALIDATION() {

	@Override
	public Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    return Collections.singleton(AdministrativeOfficeType.DEGREE);
	}

    },

    MANAGE_PHD_PROCESS_STATES() {

	@Override
	public Set<AdministrativeOfficeType> administrativeOfficeTypeContexts() {
	    return Collections.singleton(AdministrativeOfficeType.MASTER_DEGREE);
	}

    };

    abstract public Set<AdministrativeOfficeType> administrativeOfficeTypeContexts();

    public boolean isFor(final AdministrativeOfficeType officeType) {
	return administrativeOfficeTypeContexts().contains(officeType);
    }

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

    static public Collection<PermissionType> getPermissionTypes(final AdministrativeOfficeType officeType) {
	return filterPermissionTypes(officeType, new HashSet<PermissionType>());
    }

    private static Collection<PermissionType> filterPermissionTypes(final AdministrativeOfficeType officeType,
	    final Collection<PermissionType> result) {
	for (final PermissionType permissionType : values()) {
	    if (permissionType.isFor(officeType)) {
		result.add(permissionType);
	    }
	}
	return result;
    }

    static public Collection<PermissionType> getPermissionTypes(final AdministrativeOffice administrativeOffice) {
	return getPermissionTypes(administrativeOffice.getAdministrativeOfficeType());
    }

    static public Collection<PermissionType> getSortedPermissionTypes(final AdministrativeOffice administrativeOffice) {
	return filterPermissionTypes(administrativeOffice.getAdministrativeOfficeType(), new TreeSet<PermissionType>());
    }

}
