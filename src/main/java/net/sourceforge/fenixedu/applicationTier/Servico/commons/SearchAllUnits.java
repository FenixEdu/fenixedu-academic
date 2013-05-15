package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchParties;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class SearchAllUnits extends SearchParties<Unit> {

    @Override
    protected Collection<Unit> search(String value, int size) {
        Collection<UnitName> unitNames = UnitName.find(value, size);
        List<Unit> resultUnits = new ArrayList<Unit>();
        for (UnitName name : unitNames) {
            resultUnits.add(name.getUnit());
        }
        return resultUnits;
    }

}
