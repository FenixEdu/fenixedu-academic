package net.sourceforge.fenixedu.domain;

public class DegreeInfoFuture extends DegreeInfoFuture_Base {

    protected DegreeInfoFuture(final DegreeInfo degreeInfo) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setDegreeInfo(degreeInfo);
    }

}
