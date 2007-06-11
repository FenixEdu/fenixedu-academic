package net.sourceforge.fenixedu.presentationTier.Action.department;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembersType;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;
import net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit.PersistentGroupMembersBean;

public class DepartmentFunctionalities extends UnitFunctionalities {

	private static final int PAGE_SIZE = 20;
	
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
	protected PersistentGroupMembersBean getNewPersistentGroupBean(HttpServletRequest request) {
		PersistentGroupMembersBean bean = new PersistentGroupMembersBean(getUnit(request));
		bean.setType(PersistentGroupMembersType.UNIT_GROUP);
		return bean;
	}

	@Override
	protected Integer getPageSize() {
		return PAGE_SIZE;
	}

	@Override
	protected Unit getUnit(HttpServletRequest request) {
		String unitID = request.getParameter("unitId");
		DepartmentUnit unit = null;
		if (unitID != null) {
			unit = (DepartmentUnit) RootDomainObject.readDomainObjectByOID(DepartmentUnit.class, Integer
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

}
