package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "pedagogicalCouncil", path = "/pedagogicalCouncilFiles", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "uploadFile", path = "pedagogical-upload-file"),
		@Forward(name = "manageFiles", path = "pedagogical-manage-files"),
		@Forward(name = "editUploaders", path = "pedagogical-edit-uploaders"),
		@Forward(name = "managePersistedGroups", path = "manage-persisted-groups"),
		@Forward(name = "editFile", path = "pedagogical-edit-file"),
		@Forward(name = "editPersistedGroup", path = "edit-persisted-group"),
		@Forward(name = "createPersistedGroup", path = "create-persisted-group") })
public class ManageUnitFilesDA extends UnitFunctionalities {

}
