package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.assiduousness.util.RegimeType;


public class AssiduousnessRegime extends AssiduousnessRegime_Base {
        
    public AssiduousnessRegime() {
    }

    public AssiduousnessRegime(String description, String acronym) {
        setDescription(description);
        setAcronym(acronym);
    }
    
    public AssiduousnessRegime(String acronym) {
        setAcronym(acronym);
    }

    public AssiduousnessRegime(RegimeType regimeType) {
        setAcronym(regimeType.toString());
    }

    public AssiduousnessRegime(RegimeType regimeType, String description) {
        setDescription(description);
        setAcronym(regimeType.toString());
    }

    public boolean equals(AssiduousnessRegime regime) {
        return (getAcronym().equals(regime.getAcronym()) && getDescription().equals(regime.getDescription()));
    }
    
    
    
}
