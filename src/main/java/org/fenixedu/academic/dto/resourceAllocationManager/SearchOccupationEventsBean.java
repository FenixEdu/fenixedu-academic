package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.LocalDate;

public class SearchOccupationEventsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate start;
    private LocalDate end;
    private Space building;
    private List<OccupationType> types;
    private static List<OccupationType> ALL_TYPES = new ArrayList<OccupationType>();

    static {
        ALL_TYPES.addAll(Arrays.asList(OccupationType.values()));
    }

    public SearchOccupationEventsBean() {
        setTypes(ALL_TYPES);
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Space getBuilding() {
        return building;
    }

    public void setBuilding(Space building) {
        this.building = building;
    }

    public List<OccupationType> getTypes() {
        return types;
    }

    public void setTypes(List<OccupationType> types) {
        this.types = types;
    }
}
