package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.LinkObject;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

public class FindSpacesBean implements Serializable {

    private String labelToSearch;

    private Building buildingReference;

    private Campus campusReference;

    private List<LinkObject> spacePath;

    private Space selectedSpaceReference;

    private Boolean extraOptions;

    private Boolean withSchedule;

    private Boolean withWrittenEvaluations;

    private SpacesSearchCriteriaType searchType;

    private AcademicInterval academicInterval;

    public FindSpacesBean() {
        setExtraOptions(false);
        setSearchType(SpacesSearchCriteriaType.SPACE);
    }

    public FindSpacesBean(Space space, AcademicInterval academicInterval) {
        this();
        setSpace(space);
        setAcademicInterval(academicInterval);
    }

    public FindSpacesBean(Space space, SpacesSearchCriteriaType criteriaType, AcademicInterval academicInterval) {
        setSpace(space);
        setExtraOptions(false);
        setSearchType(criteriaType);
        setAcademicInterval(academicInterval);
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
        if (space != null) {
            List<Space> spaceFullPath = space.getSpaceFullPath();
            for (Space surroundingSpace : spaceFullPath) {
                result.add(new LinkObject(surroundingSpace.getExternalId(), "viewSpace", surroundingSpace.getSpaceInformation()
                        .getPresentationName()));
            }
        }
        return result;
    }

    public static enum SpacesSearchCriteriaType {

        SPACE, PERSON, EXECUTION_COURSE, WRITTEN_EVALUATION;

        public String getName() {
            return name();
        }
    }

    public void setSpacePath(List<LinkObject> spacePath) {
        this.spacePath = spacePath;
    }

    public void setSpace(Space space) {
        this.selectedSpaceReference = space;
    }

    public Space getSpace() {
        return this.selectedSpaceReference;
    }

    public void setAcademicInterval(AcademicInterval academicInterval) {
        this.academicInterval = academicInterval;
    }

    public AcademicInterval getAcademicInterval() {
        return academicInterval;
    }

    public void setBuilding(Building building) {
        this.buildingReference = building;
    }

    public Building getBuilding() {
        return this.buildingReference;
    }

    public void setCampus(Campus floor) {
        this.campusReference = floor;
    }

    public Campus getCampus() {
        return this.campusReference;
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
        if (space != null && space.isAllocatableSpace() && ((AllocatableSpace) space).isForEducation()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void setWithSchedule(Boolean withSchedule) {
        this.withSchedule = withSchedule;
    }

    public SpacesSearchCriteriaType getSearchType() {
        return searchType;
    }

    public void setSearchType(SpacesSearchCriteriaType searchType) {
        this.searchType = searchType;
    }

    public Boolean getWithWrittenEvaluations() {
        return searchType != null && searchType.equals(SpacesSearchCriteriaType.WRITTEN_EVALUATION);
    }

    public void setWithWrittenEvaluations(Boolean withWrittenEvaluations) {
        this.withWrittenEvaluations = withWrittenEvaluations;
    }
}