package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.LinkObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.Space;

public class FindSpacesBean implements Serializable {
    
    
    private String labelToSearch;
    
    private DomainReference<Building> buildingReference;
    
    private DomainReference<Campus> campusReference;

    private List<LinkObject> spacePath;
    
    private DomainReference<Space> selectedSpaceReference;
     
    private Boolean extraOptions;
    
    private Boolean withSchedule;
        

    public FindSpacesBean() {
	setExtraOptions(false);
    }
    
    public FindSpacesBean(Space space) {
	setSpace(space);
	setExtraOptions(false);
    }
    
    public List<LinkObject> getSuroundingSpacePath() {
	Space space = getSpace();	
	return space != null ? getPath(space.getSuroundingSpace()) : getPath(null);	
    }
    
    public List<LinkObject> getSpacePath() {
	return getPath(getSpace());        
    }

    private List<LinkObject> getPath(Space space) {
	List<LinkObject> result = new ArrayList<LinkObject>();
        if(space != null) {
            List<Space> spaceFullPath = space.getSpaceFullPath();
            for (Space surroundingSpace : spaceFullPath) {
		result.add(new LinkObject(surroundingSpace.getIdInternal(), "viewSpace", 
			surroundingSpace.getSpaceInformation().getPresentationName()));
	    }
        }
	return result;
    }

    public void setSpacePath(List<LinkObject> spacePath) {
        this.spacePath = spacePath;
    }
    
    public void setSpace(Space space) {
	this.selectedSpaceReference = space != null ? new DomainReference<Space>(space) : null;
    }
     
    public Space getSpace() {
	return this.selectedSpaceReference != null ? this.selectedSpaceReference.getObject() : null;
    }  
    
    public void setBuilding(Building building) {
	this.buildingReference = building != null ? new DomainReference<Building>(building) : null;
    }
     
    public Building getBuilding() {
	return this.buildingReference != null ? this.buildingReference.getObject() : null;
    }     
     
    public void setCampus(Campus floor) {
	this.campusReference = floor != null ? new DomainReference<Campus>(floor) : null;
    }
     
    public Campus getCampus() {
	return this.campusReference != null ? this.campusReference.getObject() : null;
    }

    public String getLabelToSearch() {
        return labelToSearch;
    }

    public void setLabelToSearch(String labelToSearch) {
        this.labelToSearch = labelToSearch;
    }

    public Boolean getExtraOptions() {
        return extraOptions;
    }

    public void setExtraOptions(Boolean extraOptions) {
        this.extraOptions = extraOptions;
    }
    
    public Boolean getWithSchedule() {
        Space space = getSpace();
        if(space != null && space.isAllocatableSpace() && ((AllocatableSpace)space).isForEducation()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void setWithSchedule(Boolean withSchedule) {
        this.withSchedule = withSchedule;
    }
}