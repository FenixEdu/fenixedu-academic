package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitClassification;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public class SearchResearchUnits extends AbstractSearchObjects {

    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {

	List<Unit> units = UnitUtils.readAllActiveUnitsByClassification(UnitClassification.SCIENCE_INFRASTRUCTURE);
	units.addAll(UnitUtils.readAllActiveUnitsByClassification(UnitClassification.RESEARCH_UNIT));
	units.addAll(UnitUtils.readAllActiveUnitsByClassification(UnitClassification.ASSOCIATED_LABORATORY));

	return super.process(units, value, limit, arguments);
    }
}
