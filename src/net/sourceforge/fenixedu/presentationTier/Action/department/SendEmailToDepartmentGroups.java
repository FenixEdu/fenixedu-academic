package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.SimpleMailSenderAction;

public class SendEmailToDepartmentGroups extends SimpleMailSenderAction {


	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setAttribute("unit", getDepartmentUnit(request));
		return super.execute(mapping, actionForm, request, response);
	}
	
	@Override
	protected List<IGroup> getPossibleReceivers(HttpServletRequest request) {
		List<IGroup> groups = super.getPossibleReceivers(request);

		DepartmentUnit unit = getDepartmentUnit(request);
		if (unit == null) {
			return groups;
		}

		return unit.getGroups();
	}

	protected DepartmentUnit getDepartmentUnit(HttpServletRequest request) {
		String unitID = request.getParameter("unitId");
		DepartmentUnit unit = null;
		if (unitID != null) {
			unit = (DepartmentUnit) RootDomainObject.readDomainObjectByOID(DepartmentUnit.class, Integer
					.valueOf(unitID));
		}
		return unit;
	}

}
