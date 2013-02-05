package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceState;

public class MoveSpaceBean implements Serializable {

    private Space selectedParentSpaceReference;

    private Space spaceReference;

    private SpaceState spaceState;

    private String spaceName;

    public MoveSpaceBean() {
    }

    public MoveSpaceBean(Space thisSpace) {
        setSpace(thisSpace);
        setSpaceState(SpaceState.ACTIVE);
    }

    public MoveSpaceBean(Space fromSpace, Space destinationSpace) {
        setSpace(fromSpace);
        setSelectedParentSpace(destinationSpace);
    }

    public void setSelectedParentSpace(Space parentSpace) {
        this.selectedParentSpaceReference = parentSpace;
    }

    public Space getSelectedParentSpace() {
        return this.selectedParentSpaceReference;
    }

    public void setSpace(Space thisSpace) {
        this.spaceReference = thisSpace;
    }

    public Space getSpace() {
        return this.spaceReference;
    }

    public SpaceState getSpaceState() {
        return spaceState;
    }

    public void setSpaceState(SpaceState spaceState) {
        this.spaceState = spaceState;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }
}
