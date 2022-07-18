/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto.spaceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.LinkObject;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.SharedOccupation;

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
            List<Space> spaceFullPath = getSpaceFullPath(space);
            for (Space surroundingSpace : spaceFullPath) {
                result.add(new LinkObject(surroundingSpace.getExternalId(), "viewSpace", surroundingSpace.getName()));
            }
        }
        return result;
    }
    
    private static List<Space> getSpaceFullPath(Space space) {
        List<Space> result = new ArrayList<Space>();
        result.add(space);
        Space suroundingSpace = space.getParent();
        while (suroundingSpace != null) {
            result.add(0, suroundingSpace);
            suroundingSpace = suroundingSpace.getParent();
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
        if (space != null && SpaceUtils.isRoom(space)) {
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
        return false;
    }

    public Integer getExamCapacity() {
        Optional<Integer> metadata = getSpace().getMetadata("examCapacity");
        return metadata.isPresent() ? metadata.get() : 0;
    }

    public List<User> getOccupants() {
        return getSpace().getOccupationSet().stream().filter(occ -> occ instanceof SharedOccupation && occ.isActive())
                .map(occ -> (SharedOccupation) occ).map(so -> so.getUser()).distinct().collect(Collectors.toList());
    }
}
