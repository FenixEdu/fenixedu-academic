package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.lang.StringUtils;

public class ResearchUnitSite extends ResearchUnitSite_Base {

	public ResearchUnitSite() {
		super();
		this.setShowResearchMembers(Boolean.TRUE);
		this.setShowPublications(Boolean.TRUE);
	}

	public ResearchUnitSite(ResearchUnit unit) {
		this();
		if (unit.hasSite()) {
			throw new DomainException("site.department.unit.already.has.site");
		}
		if (StringUtils.isEmpty(unit.getAcronym())) {
			throw new DomainException("unit.acronym.cannot.be.null");
		}
		this.setUnit(unit);
		new Section(this, new MultiLanguageString().new I18N().add("pt", "Lateral").add("en", "Side")
				.finish());
		new Section(this, new MultiLanguageString().new I18N().add("pt", "Topo").add("en", "Top")
				.finish());
	}

	@Override
	public ResearchUnit getUnit() {
		return (ResearchUnit) super.getUnit();
	}

	@Override
	public IGroup getOwner() {
		return new FixedSetGroup(getManagers());
	}

}
