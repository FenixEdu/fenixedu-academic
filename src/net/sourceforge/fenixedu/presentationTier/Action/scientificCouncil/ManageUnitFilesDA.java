package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/scientificCouncilFiles", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "uploadFile", path = "scientific-upload-file"),
		@Forward(name = "manageFiles", path = "scientific-manage-files"),
		@Forward(name = "editUploaders", path = "scientific-edit-uploaders"),
		@Forward(name = "managePersistedGroups", path = "manage-persisted-groups"),
		@Forward(name = "editFile", path = "scientific-edit-file"),
		@Forward(name = "editPersistedGroup", path = "edit-persisted-group"),
		@Forward(name = "createPersistedGroup", path = "create-persisted-group") })
public class ManageUnitFilesDA extends UnitFunctionalities {

}
