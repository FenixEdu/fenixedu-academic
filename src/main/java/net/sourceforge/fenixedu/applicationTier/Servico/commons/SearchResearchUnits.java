package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchParties;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class SearchResearchUnits extends SearchParties<Unit> {

    @Override
    protected Collection<Unit> search(String value, int size) {
        Collection<UnitName> unitNames = UnitName.findInternalUnitWithType(value, size, ResearchUnit.class);
        List<Unit> units = new ArrayList<Unit>();

        for (UnitName name : unitNames) {
            units.add(name.getUnit());
        }

        return units;
    }
}
