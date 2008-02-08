package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.domain.organizationalStructure.UnitClassification;

public class SearchResearchUnits extends SearchUnitByClassification {

    @Override
    protected boolean isValidClassification(UnitClassification classification) {
      return classification != null && (classification.equals(UnitClassification.SCIENCE_INFRASTRUCTURE) || classification.equals(UnitClassification.RESEARCH_UNIT) || classification.equals(UnitClassification.ASSOCIATED_LABORATORY));
    }
}
