package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.assiduousness.util.RegimeType;


public class AssiduousnessRegime extends AssiduousnessRegime_Base {
        
    public AssiduousnessRegime() {
    }

    public AssiduousnessRegime(String description, String acronym) {
        this.setDescription(description);
        this.setAcronym(acronym);
    }
    
    public AssiduousnessRegime(String acronym) {
        this.setAcronym(acronym);
    }

    public AssiduousnessRegime(RegimeType regimeType) {
        this.setAcronym(regimeType.toString());
    }

    public AssiduousnessRegime(RegimeType regimeType, String description) {
        this.setDescription(description);
        this.setAcronym(regimeType.toString());
    }

    public boolean equals(AssiduousnessRegime regime) {
        return (getAcronym().equals(regime.getAcronym()) && getDescription().equals(regime.getDescription()));
    }
    
    
    
}
