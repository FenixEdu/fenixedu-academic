package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.directiveCouncil;

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

@Mapping(module = "directiveCouncil", path = "/manageCardGeneration", scope = "session", parameter = "method")
@Forwards(value = {
		@Forward(name = "showCardGenerationProblem", path = "/directiveCouncil/cardGeneration/showCardGenerationProblem.jsp"),
		@Forward(name = "manageCardGenerationBatch", path = "/directiveCouncil/cardGeneration/manageCardGenerationBatch.jsp"),
		@Forward(name = "editDegree", path = "/directiveCouncil/cardGeneration/editDegree.jsp"),
		@Forward(name = "showCategoryCodes", path = "/directiveCouncil/cardGeneration/showCategoryCodes.jsp"),
		@Forward(name = "firstPage", path = "/directiveCouncil/cardGeneration/cardGenerationFirstPage.jsp"),
		@Forward(name = "manageCardGenerationBatchProblems", path = "/directiveCouncil/cardGeneration/manageCardGenerationBatchProblems.jsp"),
		@Forward(name = "showDegreeCodesAndLabels", path = "/directiveCouncil/cardGeneration/showDegreeCodesAndLabels.jsp") })
public class ManageCardGenerationDAForDirectiveCouncil extends net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.ManageCardGenerationDA {
}