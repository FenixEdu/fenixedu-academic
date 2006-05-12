package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;

import org.joda.time.DateTime;

public class JustificationMotive extends JustificationMotive_Base {

    public JustificationMotive(String acronym, String description, JustificationType justificationType,
            DayType dayType, JustificationGroup justificationGroup, DateTime lastModifiedDate,
            Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setAcronym(acronym);
        setDescription(description);
        setJustificationType(justificationType);
        setDayType(dayType);
        setJustificationGroup(justificationGroup);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
    }

    public JustificationMotive(String acronym, String description, DateTime lastModifiedDate,
            Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setAcronym(acronym);
        setDescription(description);
        setJustificationType(null);
        setDayType(null);
        setJustificationGroup(null);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
    }

}
