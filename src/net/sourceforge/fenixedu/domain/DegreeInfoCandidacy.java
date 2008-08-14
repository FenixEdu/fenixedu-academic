package net.sourceforge.fenixedu.domain;

public class DegreeInfoCandidacy extends DegreeInfoCandidacy_Base {

    protected DegreeInfoCandidacy(final DegreeInfo degreeInfo) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDegreeInfo(degreeInfo);
    }

}
