package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.i18n.Language;
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
        addAssociatedSections(new MultiLanguageString().with(Language.pt, "Lateral").with(Language.en, "Side"));
        addAssociatedSections(new MultiLanguageString().with(Language.pt, "Topo").with(Language.en, "Top"));
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
        StringBuilder buffer = new StringBuilder();

        for (Unit unitInPath : units) {
            if (unitInPath.getType() != PartyTypeEnum.AGGREGATE_UNIT) {
                if (buffer.length() > 0) {
                    buffer.append("/");
                }
                buffer.append(unitInPath.getAcronym());
            }
        }

        return new MultiLanguageString().with(Language.pt, buffer.toString());
    }

}
