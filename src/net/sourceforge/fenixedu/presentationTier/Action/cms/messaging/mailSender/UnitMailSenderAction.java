package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class UnitMailSenderAction extends SimpleMailSenderAction {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setAttribute("unit", getUnit(request));
		return super.execute(mapping, actionForm, request, response);
	}

	protected Unit getUnit(HttpServletRequest request) {
		Integer id = getIdInternal(request, "unitId");
		return (Unit) RootDomainObject.getInstance().readPartyByOID(id);
	}

	@Override
	protected List<IGroup> getPossibleReceivers(HttpServletRequest request) {
		List<IGroup> groups = super.getPossibleReceivers(request);

		Unit unit = getUnit(request);
		if (unit == null) {
			return groups;
		}

		return unit.getGroups();
	}

}
