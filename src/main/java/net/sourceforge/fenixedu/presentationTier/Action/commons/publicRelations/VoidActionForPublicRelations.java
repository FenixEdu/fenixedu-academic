package net.sourceforge.fenixedu.presentationTier.Action.commons.publicRelations;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publicRelations", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/publicRelations/index.jsp") })
public class VoidActionForPublicRelations extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}