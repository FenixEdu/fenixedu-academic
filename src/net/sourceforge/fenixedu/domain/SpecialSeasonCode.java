package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
/** 
 * @deprecated This entity will be removed in a future release.
 * Special season enrollments are now granted by the StudentStatute
 * (certain StudentStatuteTypes allow access to special season).
 * 
 * 
 *@see StudentStatute
 *@see StudentStatuteType
 */
public class SpecialSeasonCode extends SpecialSeasonCode_Base {

    public SpecialSeasonCode() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	if (canBeDeleted()) {
	    setRootDomainObject(null);
	    super.deleteDomainObject();
	} else {
	    throw new DomainException("error.cannot.delete.specialSeasonCode");
	}
    }

    private boolean canBeDeleted() {
	return !hasAnyYearStudentSpecialSeasonCodes();
    }

}
