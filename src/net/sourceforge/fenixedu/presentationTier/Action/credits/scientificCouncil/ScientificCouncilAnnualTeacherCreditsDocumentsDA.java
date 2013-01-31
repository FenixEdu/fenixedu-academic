package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.AnnualTeacherCreditsDocumentsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/annualTeachingCreditsDocument", scope = "request", parameter = "method")
public class ScientificCouncilAnnualTeacherCreditsDocumentsDA extends AnnualTeacherCreditsDocumentsDA {

	@Override
	public ActionForward getAnnualTeachingCreditsPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
		return getTeacherCreditsDocument(mapping, request, RoleType.SCIENTIFIC_COUNCIL);
	}
}