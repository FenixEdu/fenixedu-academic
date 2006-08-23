package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Credits extends Credits_Base {

    public Credits() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Override
    public void setDegreeModule(DegreeModule degreeModule) {
	if (degreeModule != null) {
	    throw new DomainException("can't set degree modulo for class Credits");
	}
    }

    @Override
    public void delete() {
	getEnrolments().clear();
	super.delete();
    }

    @Override
    public StringBuilder print(String tabs) {
	// TODO Auto-generated method stub
	return null;
    }

}
