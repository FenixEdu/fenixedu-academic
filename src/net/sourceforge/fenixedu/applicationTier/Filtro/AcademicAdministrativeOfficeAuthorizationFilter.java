package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 *
 */
public class AcademicAdministrativeOfficeAuthorizationFilter extends AuthorizationByRoleFilter {

    public final static AcademicAdministrativeOfficeAuthorizationFilter instance = new AcademicAdministrativeOfficeAuthorizationFilter(); 
    
    /**
     * The singleton access method of this class.
     * 
     * @return Returns the instance of this class responsible for the
     *         authorization access to services.
     */
    public static Filtro getInstance() {
        return instance;
    }
    
    @Override
    protected RoleType getRoleType() {
	return RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE;
    }

}
