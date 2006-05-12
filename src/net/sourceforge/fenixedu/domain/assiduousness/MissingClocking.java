package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class MissingClocking extends MissingClocking_Base {

    public MissingClocking(Assiduousness assiduousness, DateTime date,
            JustificationMotive justificationMotive, DateTime lastModifiedDate, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setAssiduousness(assiduousness);
        setDate(date);
        setJustificationMotive(justificationMotive);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
        setOjbConcreteClass(MissingClocking.class.getName());
    }

}
