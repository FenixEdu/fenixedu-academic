package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide.GuideListingDispatchAction;


@Mapping(path = "/guideListingByYear", module = "masterDegreeAdministrativeOffice", input = "/guide/chooseGuideYear.jsp", attribute = "chooseYearForm", formBean = "chooseYearForm")
@Forwards(value = {
	@Forward(name = "PrepareReady", path = "/guide/chooseGuideYear.jsp"),
	@Forward(name = "ShowGuideList", path = "/guide/showGuideList.jsp"),
	@Forward(name = "ShowVersionList", path = "/guide/chooseVersionPage.jsp"),
	@Forward(name = "ActionReady", path = "/guide/visualizeGuide.jsp")})
public class GuideListingDispatchAction_AM1 extends GuideListingDispatchAction {

}
