package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.space.Campus;

public class CampusBean implements Serializable {

    private Campus campus;

    public CampusBean() {
        super();
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
}
