package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembersType;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResearchUnitFunctionalities extends UnitFunctionalities {

	private static final int PAGE_SIZE = 20;
	
	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return mapping.findForward("ShowUnitFunctionalities");
	}

	
	@Override
	protected PersistentGroupMembers getGroup(HttpServletRequest request) {
		String persistedGroupID = request.getParameter("groupId");
		PersistentGroupMembers group = null;
		if (persistedGroupID != null) {
			group = (PersistentGroupMembers) RootDomainObject.readDomainObjectByOID(
					PersistentGroupMembers.class, Integer.valueOf(persistedGroupID));
		}
		return group;
	}

	@Override
	protected Unit getUnit(HttpServletRequest request) {
		String unitID = request.getParameter("unitId");
		ResearchUnit unit = null;
		if (unitID != null) {
			unit = (ResearchUnit) RootDomainObject.readDomainObjectByOID(ResearchUnit.class, Integer
					.valueOf(unitID));
		}
		return unit;
	}
	
	@Override
	protected UnitFile getUnitFile(HttpServletRequest request) {
		String fid = request.getParameter("fid");
		UnitFile file = (UnitFile) RootDomainObject.readDomainObjectByOID(UnitFile.class, Integer
				.valueOf(fid));
		return file;
	}
	
	@Override
	protected Integer getPageSize() {
		return PAGE_SIZE;
	}
	
	@Override
	protected PersistentGroupMembersBean getNewPersistentGroupBean(HttpServletRequest request) {
		PersistentGroupMembersBean bean = new PersistentGroupMembersBean(getUnit(request));
		bean.setType(PersistentGroupMembersType.UNIT_GROUP);
		return bean;
	}
}
