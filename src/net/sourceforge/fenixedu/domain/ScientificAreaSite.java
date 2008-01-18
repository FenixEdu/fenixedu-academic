package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.UnitSiteProcessor;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.lang.StringUtils;

public class ScientificAreaSite extends ScientificAreaSite_Base {

    public ScientificAreaSite() {
	super();
    }

    public ScientificAreaSite(ScientificAreaUnit unit) {
	this();
	if (unit.hasSite()) {
	    throw new DomainException("site.department.unit.already.has.site");
	}
	if (StringUtils.isEmpty(unit.getAcronym())) {
	    throw new DomainException("unit.acronym.cannot.be.null");
	}
	this.setUnit(unit);
    }

    @Override
    public ScientificAreaUnit getUnit() {
	return (ScientificAreaUnit) super.getUnit();
    }

    @Override
    public IGroup getOwner() {
	return new FixedSetGroup(getManagers());
    }

    @Override
    public MultiLanguageString getName() {
	String path = UnitSiteProcessor.getUnitSitePath(this.getUnit(), "");
	return MultiLanguageString.i18n().add("pt", path.substring(2)).finish();
    }

    @Override
    public void setNormalizedName(final MultiLanguageString normalizedName) {
	// unable to optimize because we cannot track changes to name correctly.
	// don't call super.setNormalizedName() !
    }

    @Override
    public void appendReversePathPart(final StringBuilder stringBuilder) {
	final ScientificAreaUnit unit = getUnit();
	appendReversePathPart(stringBuilder, unit);
    }

    public void appendReversePathPart(final StringBuilder stringBuilder, final Unit unit) {
	if (stringBuilder.length() > 0) {
	    stringBuilder.append('/');
	}
	Unit institutionalUnit = UnitUtils.readInstitutionUnit();
	
	for (final Unit parentUnit : unit.getParentUnitsPath()) {
	    if (!parentUnit.isAggregateUnit() && parentUnit != institutionalUnit) {
		stringBuilder.append(Content.normalize(parentUnit.getAcronym()));
		    stringBuilder.append('/');
	    }
	}
	stringBuilder.append(unit.getAcronym());
    }

}
