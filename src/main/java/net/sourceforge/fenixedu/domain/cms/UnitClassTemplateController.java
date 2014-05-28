/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.cms;

import java.util.List;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitAcronym;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public abstract class UnitClassTemplateController extends SiteTemplateController {

    private final Class<?>[] acceptableTypes;

    public UnitClassTemplateController(Class<?>[] acceptableTypes) {
        this.acceptableTypes = acceptableTypes;
    }

    @Override
    public Site selectSiteForPath(String[] possibleUnits) {
        Unit selectedUnit = Iterables.getLast(findUnitsInPath(possibleUnits), null);
        return selectedUnit != null ? selectedUnit.getSite() : null;
    }

    @Override
    public int getTrailingPath(Site site, String[] possibleUnits) {
        return findUnitsInPath(possibleUnits).size();
    }

    /**
     * Finds the units in the path on a hierarquical structure,
     * where the first unit is one of the supported classes and
     * the the unit next is a subUnit of the previous one.
     * 
     * @param possibleUnits
     *            acronyms of possible units
     * @return
     *         units organized by descendants
     */
    private List<Unit> findUnitsInPath(String[] possibleUnits) {
        List<Unit> units = Lists.newArrayList();
        Unit currentUnit = findCorrectUnit(Unit.readUnitsByAcronym(possibleUnits[0], true));

        if (currentUnit != null && isDescendantOfBaseUnit(currentUnit)) {
            units.add(currentUnit);
            for (int i = 1; i < possibleUnits.length; ++i) {
                Unit subUnit = findSubUnitByAcronym(currentUnit, possibleUnits[i]);
                if (subUnit != null) {
                    currentUnit = subUnit;
                    units.add(subUnit);
                }
            }
        }
        return units;
    }

    /**
     * @param unit
     * @param subUnitAcronym
     * @return the first subUnit of 'unit' with a 'subUnitAcronym'
     */
    private Unit findSubUnitByAcronym(final Unit unit, final String subUnitAcronym) {
        return Iterables.find(unit.getSubUnits(), new Predicate<Unit>() {
            @Override
            public boolean apply(Unit subUnit) {
                return StringUtils.equalsIgnoreCase(UnitAcronym.normalize(subUnit.getAcronym()),
                        UnitAcronym.normalize(subUnitAcronym));
            }
        }, null);
    }

    /**
     * @param units
     * @return one of the AcceptableTypes on 'units' or null if there is none
     */
    private Unit findCorrectUnit(final List<Unit> units) {
        Unit unitOfGivinType = null;
        for (final Unit unit : units) {
            for (final Class<?> clazz : getAcceptableTypes()) {
                if (clazz.equals(unit.getClass())) {
                    if (unitOfGivinType == null) {
                        unitOfGivinType = unit;
                    } else {
                        return null;
                    }
                }
            }
        }
        return unitOfGivinType;
    }

    private boolean isDescendantOfBaseUnit(Unit unit) {
        Preconditions.checkArgument(unit.getParentUnits().size() == 1, "error.unit.has.more.than.one.parent");
        for (Unit parentUnit : unit.getParentUnits()) {
            if (parentUnit == getBaseUnit()) {
                return true;
            } else {
                if (parentUnit.isAggregateUnit()) {
                    return isDescendantOfBaseUnit(parentUnit);
                }
            }
        }
        return false;
    }

    protected Unit getBaseUnit() {
        return Bennu.getInstance().getInstitutionUnit();
    }

    public Class<?>[] getAcceptableTypes() {
        return acceptableTypes;
    }

}
