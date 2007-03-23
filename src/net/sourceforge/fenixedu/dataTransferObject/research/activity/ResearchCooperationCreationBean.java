package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.CooperationType;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

import org.joda.time.YearMonthDay;

public class ResearchCooperationCreationBean implements Serializable {
			 
	private DomainReference<Cooperation> cooperation;
    private DomainReference<UnitName> unitNameObject;
    private ResearchActivityParticipationRole role;
    private CooperationType type;
    private String cooperationName;
    private String unitName;
    private CooperationUnitType cooperationUnitType;
    private YearMonthDay startDate;
    private YearMonthDay endDate;
    
    public ResearchCooperationCreationBean() {
    	setCooperation(null);
    	setUnitNameObject(null);
    } 
	
    public Cooperation getCooperation() {
		return cooperation.getObject();
	}

	public void setCooperation(Cooperation cooperation) {
		this.cooperation = new DomainReference<Cooperation>(cooperation);
	}	
    
    public CooperationType getType() {
        return type;
    }
	
    public void setType(CooperationType cooperationType) {
        this.type = cooperationType;
    }	
    
    public ResearchActivityParticipationRole getRole() {
        return role;
    }

    public void setRole(ResearchActivityParticipationRole participationRole) {
        this.role = participationRole;
    }

    public String getCooperationName() {
        return cooperationName;
    }

    public void setCooperationName(String name) {
        this.cooperationName = name;
    }
    
    public CooperationUnitType getCooperationUnitType() {
		return cooperationUnitType;
	}

	public void setCooperationUnitType(CooperationUnitType cooperationUnitType) {
		this.cooperationUnitType = cooperationUnitType;
	}
	
    public YearMonthDay getStartDate() {
        return startDate;
    }

    public void setStartDate(YearMonthDay startDate) {
        this.startDate = startDate;
    }
    
    public YearMonthDay getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }    
	
	public boolean isExternalParticipation() {
		return this.cooperationUnitType.equals(CooperationUnitType.RESEARCH_EXTERNAL_UNIT);
	}
    
    public static enum CooperationUnitType{
    	RESEARCH_INTERNAL_UNIT ("Internal"),
    	RESEARCH_EXTERNAL_UNIT ("External");
    	
    	private String type;
    	private CooperationUnitType(String type) {
    		this.type = type;
    	}
    	
    	public String getType() {
    		return type;
    	}
    }    
    
    public void setUnitName(String name) {
        this.unitName = name;
    }
    
    public String getUnitName() {
    	return unitName;
    }
    
    public UnitName getUnitNameObject() {
    	return unitNameObject.getObject();
    }
    
    public void setUnitNameObject(UnitName unitName) {
    	this.unitNameObject = new DomainReference<UnitName>(unitName);
    }
    
    public Unit getUnit() {
    	UnitName unitName = getUnitNameObject();
    	return (unitName!=null) ? unitName.getUnit() : null; 
    }
    
    public void setUnit (Unit organization) {
    	UnitName unitName = null;
    	if(organization!=null)  {
    		unitName = organization.getUnitName();
    	}
    	setUnitNameObject(unitName);
    } 
}
