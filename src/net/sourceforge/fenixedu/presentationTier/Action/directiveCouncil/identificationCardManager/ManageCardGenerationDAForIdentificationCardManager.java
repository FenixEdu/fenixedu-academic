package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.identificationCardManager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "identificationCardManager", path = "/manageCardGeneration", scope = "session", parameter = "method")
@Forwards(
		value = {
				@Forward(
						name = "editProfessionalCategory",
						path = "/identificationCardManager/cardGeneration/editProfessionalCategory.jsp"),
				@Forward(name = "uploadCardInfo", path = "/identificationCardManager/cardGeneration/uploadCardInfo.jsp"),
				@Forward(
						name = "showCardGenerationProblem",
						path = "/identificationCardManager/cardGeneration/showCardGenerationProblem.jsp"),
				@Forward(
						name = "manageCardGenerationBatch",
						path = "/identificationCardManager/cardGeneration/manageCardGenerationBatch.jsp"),
				@Forward(name = "editDegree", path = "/identificationCardManager/cardGeneration/editDegree.jsp"),
				@Forward(name = "showCategoryCodes", path = "/identificationCardManager/cardGeneration/showCategoryCodes.jsp"),
				@Forward(name = "firstPage", path = "/identificationCardManager/cardGeneration/cardGenerationFirstPage.jsp"),
				@Forward(
						name = "editCardGenerationBatch",
						path = "/identificationCardManager/cardGeneration/editCardGenerationBatch.jsp"),
				@Forward(
						name = "manageCardGenerationBatchProblems",
						path = "/identificationCardManager/cardGeneration/manageCardGenerationBatchProblems.jsp"),
				@Forward(name = "CrossReferenceBean", path = "/identificationCardManager/CrossReferenceBean.jsp"),
				@Forward(
						name = "showDegreeCodesAndLabels",
						path = "/identificationCardManager/cardGeneration/showDegreeCodesAndLabels.jsp") })
public class ManageCardGenerationDAForIdentificationCardManager extends
		net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.ManageCardGenerationDA {
}