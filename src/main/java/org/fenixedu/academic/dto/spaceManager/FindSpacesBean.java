package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.sourceforge.fenixedu.dataTransferObject.LinkObject;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;

public class FindSpacesBean implements Serializable {

    private String labelToSearch;

    private Space buildingReference;

    private Space campusReference;

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
        return space != null ? getPath(space.getParent()) : getPath(null);
    }

    public List<LinkObject> getSpacePath() {
        return getPath(getSpace());
    }

    private List<LinkObject> getPath(Space space) {
        List<LinkObject> result = new ArrayList<LinkObject>();
        if (space != null) {
            List<Space> spaceFullPath = SpaceUtils.getSpaceFullPath(space);
            for (Space surroundingSpace : spaceFullPath) {
                result.add(new LinkObject(surroundingSpace.getExternalId(), "viewSpace", surroundingSpace.getName()));
            }
        }
        return result;
    }

    public static enum SpacesSearchCriteriaType {

        SPACE, /*PERSON,*/EXECUTION_COURSE, WRITTEN_EVALUATION;

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

    public void setBuilding(Space building) {
        this.buildingReference = building;
    }

    public Space getBuilding() {
        return this.buildingReference;
    }

    public void setCampus(Space floor) {
        this.campusReference = floor;
    }

    public Space getCampus() {
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
        if (space != null && (SpaceUtils.isRoom(space) || SpaceUtils.isRoomSubdivision(space))
                && SpaceUtils.isForEducation(space)) {
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

    public Boolean getIsOccupiedByWrittenEvaluations() {
        for (Occupation occupation : getSpace().getOccupationSet()) {
            if (occupation instanceof WrittenEvaluationSpaceOccupation) {
                return true;
            }
        }
        return false;
    }

    public Integer getExamCapacity() {
        Optional<Integer> metadata = getSpace().getMetadata("examCapacity");
        return metadata.isPresent() ? metadata.get() : 0;
    }
}
