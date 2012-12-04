package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/personnelUnitFunctionalities", module = "personnelSection")
@Forwards({
	@Forward(name = "managePersistedGroups", path = "manage-persisted-groups", tileProperties = @Tile(title = "private.staffarea.communication.groups")),
	@Forward(name = "createPersistedGroup", path = "create-persisted-group", tileProperties = @Tile(title = "private.staffarea.communication.groups")),
	@Forward(name = "editPersistedGroup", path = "edit-persisted-group", tileProperties = @Tile(title = "private.staffarea.communication.groups")),
	@Forward(name = "uploadFile", path = "upload-file", tileProperties = @Tile(title = "private.staffarea.communication.groups")),
	@Forward(name = "manageFiles", path = "manage-files", tileProperties = @Tile(title = "private.staffarea.communication.groups")),
	@Forward(name = "editFile", path = "edit-file", tileProperties = @Tile(title = "private.staffarea.communication.groups")),
	@Forward(name = "editUploaders", path = "edit-uploaders", tileProperties = @Tile(title = "private.staffarea.communication.groups")) })
public class PersonnelSectionUnitFunctionalities extends UnitFunctionalities {

    private static Unit personnelSectionUnit = null;

    @Override
    protected Unit getUnit(HttpServletRequest request) {
	if (personnelSectionUnit == null) {
	    personnelSectionUnit = super.getUnit(request);
	    if (personnelSectionUnit == null) {
		personnelSectionUnit = Unit.readByCostCenterCode(6231);
	    }
	}

	return personnelSectionUnit;
    }
}
