package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitAcronym;

public abstract class AbstractAcronymPathProcess<T extends Site> extends AbstractPathProcessor {

	protected T site = null;

	@Override
	public Content processPath(String path) {
		return getSite();
	}

	private Content getSite() {
		if (site == null) {
			for (UnitAcronym acronym : RootDomainObject.getInstance().getUnitAcronyms()) {
				if (acronym.getAcronym().equalsIgnoreCase(getAcronym())) {
					Unit unit = acronym.getUnits().get(0);
					site = (T) unit.getSite();
					return site;
				}
			}
		}
		return site;
	}

	protected abstract String getAcronym();

	@Override
	public String getTrailingPath(String path) {
		return path;
	}

	@Override
	public Content getInitialContent() {
		return getSite();
	}

	@Override
	public boolean keepPortalInContentsPath() {
		return false;
	}
}
