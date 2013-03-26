package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.space.Building;

import org.joda.time.LocalDate;

public class SearchSpaceEventsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate start;
    private LocalDate end;
    private Building building;
    private List<OccupationType> types;
    private static List<OccupationType> ALL_TYPES = new ArrayList<OccupationType>();

    static {
        ALL_TYPES.addAll(Arrays.asList(OccupationType.values()));
    }

    public SearchSpaceEventsBean() {
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

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public List<OccupationType> getTypes() {
        return types;
    }

    public void setTypes(List<OccupationType> types) {
        this.types = types;
    }
}
