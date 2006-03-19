package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class MissingClocking extends MissingClocking_Base {

    public MissingClocking() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

}
