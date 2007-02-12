package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;

public class ResearchCooperationEditionBean extends ResearchActivityEditionBean implements Serializable {
	private DomainReference<Cooperation> cooperation;
    
    public ResearchCooperationEditionBean() {
    	setCooperation(null);
    }
    
    public Cooperation getCooperation() {
		return cooperation.getObject();
	}

	public void setCooperation(Cooperation cooperation) {
		this.cooperation = new DomainReference<Cooperation>(cooperation);
	} 
}
