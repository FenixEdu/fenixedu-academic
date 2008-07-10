package net.sourceforge.fenixedu.dataTransferObject.residenceManagement;

import java.io.Serializable;
import java.util.List;

public class ResidentListsHolderBean implements Serializable {

    List<ResidenceEventBean> successfulEvents;
    List<ResidenceEventBean> unsuccessfulEvents;
    
    public List<ResidenceEventBean> getSuccessfulEvents() {
        return successfulEvents;
    }

    public List<ResidenceEventBean> getUnsuccessfulEvents() {
        return unsuccessfulEvents;
    }

    public ResidentListsHolderBean(List<ResidenceEventBean> sucessful,  List<ResidenceEventBean> unsucessful) {
	this.successfulEvents = sucessful;
	this.unsuccessfulEvents = unsucessful;
    }
    
    public Integer getNumberOfImports() {
	return successfulEvents.size() + unsuccessfulEvents.size();
    }
}
