package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivity;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class ResearchActivityUnitParticipantBean implements Serializable {
	private DomainReference<ResearchActivity> activity;
    private DomainReference<Unit> unit;
    private ResearchActivityParticipationRole role;
    private String unitName;
    private UnitParticipationType unitParticipationType;
    
    public ResearchActivityUnitParticipantBean() {
    	setUnit(null);
    	setResearchActivity(null);
    }
    
    public ResearchActivity getResearchActivity() {
		return activity.getObject();
	}

	public void setResearchActivity(ResearchActivity activity) {
		this.activity = new DomainReference<ResearchActivity>(activity);
	}

	public ResearchActivityParticipationRole getRole() {
        return role;
    }

    public void setRole(ResearchActivityParticipationRole role) {
        this.role = role;
    }

    public void setUnitName(String name) {
        this.unitName = name;
    }
    
    public String getUnitName() {
    	return unitName;
    }
    
    public Unit getUnit() {
        return this.unit.getObject();
    }
    
    public void setUnit (Unit organization) {
        this.unit = new DomainReference<Unit>(organization);
    }
    
    public UnitParticipationType getParticipationType() {
		return unitParticipationType;
	}

	public void setParticipationType(UnitParticipationType unitParticipationType) {
		this.unitParticipationType = unitParticipationType;
	}
	
	public boolean isExternalParticipation() {
		return this.unitParticipationType.equals(UnitParticipationType.EXTERNAL_UNIT);
	}
    
    public static enum UnitParticipationType{
    	INTERNAL_UNIT ("Internal"),
    	EXTERNAL_UNIT ("External");
    	
    	private String type;
    	private UnitParticipationType(String type) {
    		this.type = type;
    	}
    	
    	public String getType() {
    		return type;
    	}
    }
}
