package net.sourceforge.fenixedu.presentationTier.Action.commons.parkingManager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "parkingManager", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "parkingManagerIndex") })
public class VoidActionForParkingManager extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}