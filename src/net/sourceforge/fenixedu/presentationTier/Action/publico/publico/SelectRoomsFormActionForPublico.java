package net.sourceforge.fenixedu.presentationTier.Action.publico.publico;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "publico",
		path = "/chooseRoomsFormNew",
		input = "/prepareConsultRoomsNew.do?method=prepare&page=0",
		attribute = "roomForm",
		formBean = "roomForm",
		scope = "request")
@Forwards(value = { @Forward(name = "one", path = "/siteViewerRoomNew.do?method=roomViewer"),
		@Forward(name = "list", path = "chooseRoom") })
public class SelectRoomsFormActionForPublico extends
		net.sourceforge.fenixedu.presentationTier.Action.publico.SelectRoomsFormAction {
}