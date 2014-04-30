package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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

        Unit institutionalUnit = UnitUtils.readInstitutionUnit();

        for (final Unit parentUnit : getUnit().getParentUnitsPath()) {
            if (!parentUnit.isAggregateUnit() && parentUnit != institutionalUnit) {
                stringBuilder.append(CmsContent.normalize(parentUnit.getAcronym()));
                stringBuilder.append('/');
            }
        }
        stringBuilder.append(CmsContent.normalize(getUnit().getAcronym()));

        return stringBuilder.toString();
    }

}
