package net.sourceforge.fenixedu.presentationTier.Action.phd.program.manager;

import net.sourceforge.fenixedu.presentationTier.Action.phd.program.PhdProgramInformationDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgramInformation", module = "manager")
@Forwards({
	@Forward(name = "listPhdPrograms", path = "/phd/manager/program/information/listPhdPrograms.jsp"),
	@Forward(name = "listPhdProgramInformations", path = "/phd/manager/program/information/listPhdProgramInformations.jsp")
 })
public class PhdProgramInformationDAForManager extends PhdProgramInformationDA {

}
