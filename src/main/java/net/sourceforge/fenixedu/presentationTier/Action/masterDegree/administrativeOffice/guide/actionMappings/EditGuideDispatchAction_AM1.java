package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide.EditGuideDispatchAction;


@Mapping(path = "/editGuideInformation", module = "masterDegreeAdministrativeOffice", input = "/guide/editGuideInformation.jsp", attribute = "editGuideInformationForm", formBean = "editGuideInformationForm")
@Forwards(value = {
	@Forward(name = "PrepareReady", path = "/guide/editGuideInformation.jsp"),
	@Forward(name = "EditInformationSuccess", path = "/guide/visualizeGuide.jsp")})
@Exceptions(value = {
	@ExceptionHandling(key = "resources.Action.exceptions.InvalidChangeActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidChangeActionException.class),
	@ExceptionHandling(key = "resources.Action.exceptions.NoChangeMadeActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NoChangeMadeActionException.class),
	@ExceptionHandling(key = "resources.Action.exceptions.InvalidInformationInFormActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidInformationInFormActionException.class)})
public class EditGuideDispatchAction_AM1 extends EditGuideDispatchAction {

}
