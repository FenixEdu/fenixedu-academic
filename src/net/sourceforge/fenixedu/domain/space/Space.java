package net.sourceforge.fenixedu.domain.space;

import java.util.List;

public class Space extends Space_Base {

    public Space() {
        super();
        new SpaceInformation(this);
    }

}
