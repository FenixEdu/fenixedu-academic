package net.sourceforge.fenixedu.domain.space;


public class Space extends Space_Base {

    public Space() {
        super();
        new SpaceInformation(this);
    }

}
