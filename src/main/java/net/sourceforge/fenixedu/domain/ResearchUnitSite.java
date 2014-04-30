package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
        addAssociatedSections(new MultiLanguageString().with(MultiLanguageString.pt, "Lateral").with(MultiLanguageString.en,
                "Side"));
        addAssociatedSections(new MultiLanguageString().with(MultiLanguageString.pt, "Topo").with(MultiLanguageString.en, "Top"));
    }

    @Override
    public ResearchUnit getUnit() {
        return (ResearchUnit) super.getUnit();
    }

    @Override
    public Group getOwner() {
        return UserGroup.of(Person.convertToUsers(getManagers()));
    }

    @Override
    public MultiLanguageString getName() {
        Unit unit = this.getUnit();
        List<Unit> units = unit.getParentUnitsPath(false);
        units.add(unit);
        StringBuilder buffer = new StringBuilder();

        for (Unit unitInPath : units) {
            if (unitInPath.getType() != PartyTypeEnum.AGGREGATE_UNIT) {
                if (buffer.length() > 0) {
                    buffer.append("/");
                }
                buffer.append(unitInPath.getAcronym());
            }
        }

        return new MultiLanguageString().with(MultiLanguageString.pt, buffer.toString());
    }

    @Override
    public String getReversePath() {
        StringBuilder stringBuilder = new StringBuilder(super.getReversePath()).append('/');
        for (final Unit parentUnit : getUnit().getParentUnitsPath()) {
            if (parentUnit.isResearchUnit()) {
                stringBuilder.append(CmsContent.normalize(parentUnit.getAcronym()));
                stringBuilder.append('/');
            }
        }
        stringBuilder.append(getUnit().getAcronym());
        return stringBuilder.toString();
    }

}
