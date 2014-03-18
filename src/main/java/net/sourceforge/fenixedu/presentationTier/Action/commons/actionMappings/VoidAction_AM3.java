package net.sourceforge.fenixedu.presentationTier.Action.commons.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction;


@Mapping(path = "/backErrorCandidateApprovalPage", module = "coordinator")
@Forwards(value = {
	@Forward(name = "Success", path = "/candidate/backErrorPage.jsp")})
public class VoidAction_AM3 extends VoidAction {

}
