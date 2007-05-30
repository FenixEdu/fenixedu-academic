package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.SimpleMailSenderAction;

public class SendEmailToResearchUnitGroups extends SimpleMailSenderAction {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setAttribute("unit", getResearchUnit(request));
		return super.execute(mapping, actionForm, request, response);
	}

	@Override
	protected List<IGroup> getPossibleReceivers(HttpServletRequest request) {
		List<IGroup> groups = super.getPossibleReceivers(request);

		ResearchUnit unit = getResearchUnit(request);
		if (unit == null) {
			return groups;
		}

		return unit.getGroups();
	}

	private ResearchUnit getResearchUnit(HttpServletRequest request) {
		String unitID = request.getParameter("unitId");
		ResearchUnit unit = null;
		if (unitID != null) {
			unit = (ResearchUnit) RootDomainObject.readDomainObjectByOID(ResearchUnit.class, Integer
					.valueOf(unitID));
		}
		return unit;
	}
}
