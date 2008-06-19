package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class AssiduousnessCampusHistory extends AssiduousnessCampusHistory_Base {

    public AssiduousnessCampusHistory(Assiduousness assiduousness, Campus campus, LocalDate beginDate, LocalDate endDate,
	    DateTime lastModifiedDate, Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setCampus(campus);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
    }

}
