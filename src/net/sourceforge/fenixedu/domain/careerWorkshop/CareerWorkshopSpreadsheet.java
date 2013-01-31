package net.sourceforge.fenixedu.domain.careerWorkshop;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CareerWorkshopSpreadsheet extends CareerWorkshopSpreadsheet_Base {

	private static final String ROOT_DIR_DESCRIPTION = "IST Career Workshops";

	private static final String ROOT_DIR = "CareerWorkshops";

	private static final String SHEETS_DIR = "Sheet";

	private static final String SHEETS_DIR_DESCRIPTION = "Spreadsheets";

	public CareerWorkshopSpreadsheet(String filename, byte[] content) {
		super();
		RoleGroup cg = new RoleGroup(Role.getRoleByRoleType(RoleType.DIRECTIVE_COUNCIL));
		init(getVirtualPath(), filename, filename, null, content, cg);
	}

	private VirtualPath getVirtualPath() {
		final VirtualPath filePath = new VirtualPath();
		filePath.addNode(new VirtualPathNode(ROOT_DIR, ROOT_DIR_DESCRIPTION));
		filePath.addNode(new VirtualPathNode(SHEETS_DIR, SHEETS_DIR_DESCRIPTION));
		return filePath;
	}

	@Override
	public void delete() {
		removeCareerWorkshopApplicationEvent();
		super.delete();
	}

}
