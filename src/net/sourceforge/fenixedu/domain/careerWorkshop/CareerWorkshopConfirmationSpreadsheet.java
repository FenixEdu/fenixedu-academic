package net.sourceforge.fenixedu.domain.careerWorkshop;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CareerWorkshopConfirmationSpreadsheet extends CareerWorkshopConfirmationSpreadsheet_Base {
    
    private static final String ROOT_DIR_DESCRIPTION = "IST Career Workshops";

    private static final String ROOT_DIR = "CareerWorkshops";
    
    public  CareerWorkshopConfirmationSpreadsheet(String filename, byte[] content) {
        super();
        RoleGroup cg = new RoleGroup(Role.getRoleByRoleType(RoleType.DIRECTIVE_COUNCIL));
	init(getVirtualPath(), filename, filename, null, content, cg);
    }
    
    private VirtualPath getVirtualPath() {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode(ROOT_DIR, ROOT_DIR_DESCRIPTION));
	return filePath;
    }
    
}
