package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "pedagogicalCouncil", path = "/pedagogicalCouncilFiles", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "uploadFile", path = "pedagogical-upload-file", tileProperties = @Tile(title = "private.pedagogiccouncil.communication.files")),
	@Forward(name = "manageFiles", path = "pedagogical-manage-files", tileProperties = @Tile(title = "private.pedagogiccouncil.communication.files")),
	@Forward(name = "editUploaders", path = "pedagogical-edit-uploaders", tileProperties = @Tile(title = "private.pedagogiccouncil.communication.files")),
	@Forward(name = "managePersistedGroups", path = "manage-persisted-groups", tileProperties = @Tile(title = "private.pedagogiccouncil.communication.files")),
	@Forward(name = "editFile", path = "pedagogical-edit-file", tileProperties = @Tile(title = "private.pedagogiccouncil.communication.files")),
	@Forward(name = "editPersistedGroup", path = "edit-persisted-group", tileProperties = @Tile(title = "private.pedagogiccouncil.communication.files")),
	@Forward(name = "createPersistedGroup", path = "create-persisted-group", tileProperties = @Tile(title = "private.pedagogiccouncil.communication.files")) })
public class ManageUnitFilesDA extends UnitFunctionalities {

}
