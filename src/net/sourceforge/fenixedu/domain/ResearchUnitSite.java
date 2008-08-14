package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.apache.commons.lang.StringUtils;

public class ResearchUnitSite extends ResearchUnitSite_Base {

    public ResearchUnitSite() {
	super();
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
	if (StringUtils.isEmpty(unit.getAcronym())) {
	    throw new DomainException("unit.acronym.cannot.be.null");
	}
	this.setUnit(unit);
	new Section(this, new MultiLanguageString().new I18N().add("pt", "Lateral").add("en", "Side").finish());
	new Section(this, new MultiLanguageString().new I18N().add("pt", "Topo").add("en", "Top").finish());
    }

    @Override
    public ResearchUnit getUnit() {
	return (ResearchUnit) super.getUnit();
    }

    @Override
    public IGroup getOwner() {
	return new FixedSetGroup(getManagers());
    }

    @Override
    public MultiLanguageString getName() {
	Unit unit = this.getUnit();
	List<Unit> units = unit.getParentUnitsPath(false);
	units.add(unit);
	StringBuffer buffer = new StringBuffer("");

	for (Unit unitInPath : units) {
	    if (unitInPath.getType() != PartyTypeEnum.AGGREGATE_UNIT) {
		if (buffer.length() > 0) {
		    buffer.append("/");
		}
		buffer.append(unitInPath.getAcronym());
	    }
	}

	return MultiLanguageString.i18n().add("pt", buffer.toString()).finish();
    }

    @Override
    public void setNormalizedName(final MultiLanguageString normalizedName) {
	// unable to optimize because we cannot track changes to name correctly.
	// don't call super.setNormalizedName() !
    }

    @Override
    public void appendReversePathPart(final StringBuilder stringBuilder) {
	final ResearchUnit researchUnit = getUnit();
	appendReversePathPart(stringBuilder, researchUnit);
    }

    public void appendReversePathPart(final StringBuilder stringBuilder, final Unit unit) {
	if (stringBuilder.length() > 0) {
	    stringBuilder.append('/');
	}
	for (final Unit parentUnit : unit.getParentUnitsPath()) {
	    if (parentUnit.isResearchUnit()) {
		stringBuilder.append(Content.normalize(parentUnit.getAcronym()));
		stringBuilder.append('/');
	    }
	}
	stringBuilder.append(unit.getAcronym());
    }

}
