package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class JustificationMotive extends JustificationMotive_Base {

    // construtors used in scripts
    public JustificationMotive(String acronym, String description, Boolean actualWorkTime,
	    JustificationType justificationType, DayType dayType, JustificationGroup justificationGroup,
	    DateTime lastModifiedDate, Employee modifiedBy) {
	super();
	init(acronym, description, actualWorkTime, justificationType, dayType, justificationGroup,
		lastModifiedDate, modifiedBy);
    }

    public JustificationMotive(String acronym, String description, DateTime lastModifiedDate,
	    Employee modifiedBy) {
	super();
	init(acronym, description, false, null, null, null, lastModifiedDate, modifiedBy);
    }

    private void init(String acronym, String description, Boolean actualWorkTime,
	    JustificationType justificationType, DayType dayType, JustificationGroup justificationGroup,
	    DateTime lastModifiedDate, Employee modifiedBy) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAcronym(acronym);
	setDescription(description);
	setJustificationType(justificationType);
	setDayType(dayType);
	setJustificationGroup(justificationGroup);
	setActualWorkTime(actualWorkTime);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
    }

    public JustificationMotive(String acronym, String description, Boolean actualWorkTime,
	    JustificationType justificationType, DayType dayType, JustificationGroup justificationGroup,
	    Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym)) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	init(acronym, description, actualWorkTime, justificationType, dayType, justificationGroup,
		new DateTime(), modifiedBy);
    }

    // in regularizations actualWorkTime is always false
    public JustificationMotive(String acronym, String description, JustificationType justificationType,
	    DayType dayType, JustificationGroup justificationGroup, Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym)) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	init(acronym, description, false, justificationType, dayType, justificationGroup,
		new DateTime(), modifiedBy);
    }

    public JustificationMotive(String acronym, String description, Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym)) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	init(acronym, description, false, null, null, null, new DateTime(), modifiedBy);
    }

    private boolean alreadyExistsJustificationMotiveAcronym(String acronym) {
	return alreadyExistsJustificationMotiveAcronym(acronym, null);
    }

    private boolean alreadyExistsJustificationMotiveAcronym(String acronym, Integer id) {
	for (JustificationMotive justificationMotive : RootDomainObject.getInstance()
		.getJustificationMotives()) {
	    if (justificationMotive.getAcronym().equalsIgnoreCase(acronym)
		    && (id == null || !getIdInternal().equals(id))) {
		return true;
	    }
	}
	return false;
    }

    public void editJustificationMotive(String acronym, String description, Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym, getIdInternal())) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	setAcronym(acronym);
	setDescription(description);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
    }

    public void editJustificationMotive(String acronym, String description, Boolean actualWorkTime,
	    JustificationType justificationType, DayType dayType, JustificationGroup justificationGroup,
	    Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym, getIdInternal())) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	setAcronym(acronym);
	setDescription(description);
	setJustificationType(justificationType);
	setDayType(dayType);
	setJustificationGroup(justificationGroup);
	setActualWorkTime(actualWorkTime);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
    }
}