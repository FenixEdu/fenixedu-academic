package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class ManagementCouncilSiteProcessor extends AbstractPathProcessor {

	@Override
	public Content processPath(String path) {
		return getManagementCouncilUnit().getSite();
	}

	@Override
	public String getTrailingPath(String path) {
		return path;
	}

	@Override
	public Content getInitialContent() {
		return getManagementCouncilUnit().getSite();
	}

	@Override
	public boolean keepPortalInContentsPath() {
		return false;
	}

	private Unit getManagementCouncilUnit() {
		return (Unit) PartyType.getPartiesSet(PartyTypeEnum.MANAGEMENT_COUNCIL).iterator().next();
	}
}
